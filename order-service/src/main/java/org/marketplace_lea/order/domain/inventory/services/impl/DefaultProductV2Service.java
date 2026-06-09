package org.marketplace_lea.order.domain.inventory.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.common.service.storage.StorageService;
import org.marketplace_lea.order.common.entities.inventory.ProductV2Entity;
import org.marketplace_lea.order.common.repository.inventory.ProductV2JpaRepository;
import org.marketplace_lea.order.common.repository.order.CartItemV2JpaRepository;
import org.marketplace_lea.order.domain.inventory.dto.CreateProductV2Form;
import org.marketplace_lea.order.domain.inventory.dto.ProductV2Criteria;
import org.marketplace_lea.order.domain.inventory.dto.ProductV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateProductV2Form;
import org.marketplace_lea.order.domain.inventory.mapper.ProductV2Mapper;
import org.marketplace_lea.order.domain.inventory.services.ProductV2Service;
import org.marketplace_lea.order.domain.inventory.services.specifications.ProductV2Specification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.marketplace_lea.common.common.utils.FileStorageUtils.createNewFileName;
import static org.marketplace_lea.common.common.utils.MediaUtils.getProductImageKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultProductV2Service implements ProductV2Service {
    private final ProductV2JpaRepository productRepository;
    private final StorageService storageService;
    private final CartItemV2JpaRepository cartItemV2JpaRepository;
    private final ProductV2Mapper productV2Mapper;

    private ProductV2Entity getEntityById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Produit introuvable: " + id));
    }

    @Override
    public ProductV2DTO getById(String id) {
        return productV2Mapper.toDTO(getEntityById(id));
    }

    @Override
    public Optional<ProductV2DTO> findById(String id) {
        return productRepository.findById(id)
                .map(productV2Mapper::toDTO);
    }

    @Override
    public Page<ProductV2DTO> searchProducts(ProductV2Criteria criteria, Pageable pageable) {
        Specification<ProductV2Entity> spec = ProductV2Specification.buildSpecification(criteria);
        return productRepository.findAll(spec, pageable)
                .map(productV2Mapper::toDTO);
    }

    @Override
    @Transactional
    public ProductV2DTO createProduct(CreateProductV2Form form, MultipartFile image, MultipartFile[] gallery) {
        log.info("[DefaultProductV2Service.createProduct] Création d'un nouveau produit: {}", form.label());

        ProductV2Entity product = productV2Mapper.toEntity(form);

        if (product.isActiveCashback()) {
            product.setSavingAmount(form.savingAmount());
        } else {
            product.setSavingAmount(BigDecimal.ZERO);
        }

        int rank = (int) productRepository.count() + 1;
        product.setRank(rank);

        uploadProductImage(product, image);

        var productSaved = productRepository.save(product);
        log.info("Create new Product: {}", productSaved.getId());
        return productV2Mapper.toDTO(productSaved);
    }

    @Override
    @Transactional
    public ProductV2DTO updateProduct(String id, UpdateProductV2Form form, MultipartFile image) {
        log.info("[DefaultProductV2Service.updateProduct] Mise à jour du produit: {}", id);
        ProductV2Entity product = getEntityById(id);

        if (form != null) {
            productV2Mapper.mapToEntity(form, product);
            if (product.isActiveCashback()) {
                product.setSavingAmount(form.savingAmount());
            } else {
                product.setSavingAmount(BigDecimal.ZERO);
            }
        }

        if (image != null) {
            uploadProductImage(product, image);
        }

        var productV2Saved = productRepository.save(product);
        log.info("[DefaultProductV2Service.updateProduct] Update product: {}", product.getId());
        return productV2Mapper.toDTO(productV2Saved);
    }

    @Override
    @Transactional
    public void updateRank(String currentProductId, String referralProductId) {
        ProductV2Entity current = getEntityById(currentProductId);
        var prevOrSupProductFound = productRepository.findById(referralProductId);

        prevOrSupProductFound.ifPresent(productV2Entity -> {
            int prevOrSupRank = productV2Entity.getRank();
            productV2Entity.setRank(current.getRank());
            current.setRank(prevOrSupRank);
            productRepository.saveAll(List.of(current, productV2Entity));
        });
    }

    @Override
    @Transactional
    public void updateRank(String currentProductId, int position) {
        ProductV2Entity current = getEntityById(currentProductId);
        ProductV2Entity toChange = productRepository.findByRank(position)
                .orElse(null);

        if (toChange == null || toChange.getRank() == 0) {
            current.setRank(position);
            productRepository.save(current);
        } else {
            int toChangeRank = toChange.getRank();
            toChange.setRank(current.getRank());
            current.setRank(toChangeRank);
            productRepository.saveAll(List.of(current, toChange));
        }
    }

    @Override
    @Transactional
    public void performAction(String productId, String action, String referralProductId) {
        ProductV2Entity product = getEntityById(productId);

        if (action.equals("soldout")) {
            product.setSoldOut(true);
            productRepository.save(product);
        }

        if (action.equals("available")) {
            product.setSoldOut(false);
            productRepository.save(product);
        }

        if (action.equals("rankUp") && referralProductId != null && !referralProductId.isEmpty()) {
            updateRank(productId, referralProductId);
        }

        if (action.equals("rankDown") && referralProductId != null && !referralProductId.isEmpty()) {
            updateRank(productId, referralProductId);
        }
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        cartItemV2JpaRepository.deleteByProductId(id);
        ProductV2Entity product = getEntityById(id);
        product.setDeletedAt(LocalDateTime.now());
        var productDeleted = productRepository.save(product);
        log.info("[DefaultProductV2Service.deleteById] Product deleted: {}", productDeleted.getId());
    }

    @Override
    @Transactional
    public void delete(ProductV2Entity product) {
        log.info("Deleting product with id {}", product.getId());
        deleteById(product.getId());
    }

    @Override
    @Transactional
    public void changeRank(String productId, int newRank) {
        ProductV2Entity product = getEntityById(productId);
        product.setRank(newRank);
        productRepository.save(product);
    }

    private void uploadProductImage(ProductV2Entity product, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return;
        }
        String fileName = createNewFileName(image.getOriginalFilename());
        product.setImage(fileName);
        try {
            storageService.store(image, getProductImageKey(product.getId(), fileName));
        } catch (Exception e) {
            log.error("Erreur de sauvegarde de l'image principale", e);
            throw new RuntimeException("Erreur de sauvegarde de l'image principale", e);
        }
    }
}

