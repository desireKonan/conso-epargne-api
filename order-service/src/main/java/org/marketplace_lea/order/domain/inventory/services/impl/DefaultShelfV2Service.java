package org.marketplace_lea.order.domain.inventory.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.common.service.storage.StorageService;
import org.marketplace_lea.order.common.entities.inventory.ShelfV2Entity;
import org.marketplace_lea.order.common.repository.inventory.EnsignV2JpaRepository;
import org.marketplace_lea.order.common.repository.inventory.ShelfV2JpaRepository;
import org.marketplace_lea.order.domain.inventory.dto.CreateShelfV2Form;
import org.marketplace_lea.order.domain.inventory.dto.ShelfV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateShelfV2Form;
import org.marketplace_lea.order.domain.inventory.mapper.ShelfV2Mapper;
import org.marketplace_lea.order.domain.inventory.services.ShelfV2Service;
import org.marketplace_lea.order.domain.inventory.services.specifications.ShelfV2Specification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.marketplace_lea.common.common.utils.FileStorageUtils.createNewFileName;
import static org.marketplace_lea.common.common.utils.MediaUtils.getShelfImageKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultShelfV2Service implements ShelfV2Service {
    private final ShelfV2JpaRepository shelfRepository;
    private final EnsignV2JpaRepository ensignRepository;
    private final StorageService storageService;
    private final ShelfV2Mapper shelfV2Mapper;

    private ShelfV2Entity getEntityById(String id) {
        return shelfRepository.findById(id)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Rayon introuvable: " + id));
    }

    @Override
    @Transactional
    public ShelfV2DTO createShelf(CreateShelfV2Form form, MultipartFile image) {
        log.info("[DefaultShelfV2Service.createShelf] Création d'un nouveau rayon: {}", form.label());

        ShelfV2Entity shelf = shelfV2Mapper.toEntity(form);
        shelf.setRank((int) shelfRepository.count() + 1);

        if (image != null && !image.isEmpty()) {
            uploadShelfImage(shelf, image);
        } else {
            throw new IllegalArgumentException("L'image du rayon est obligatoire");
        }

        return shelfV2Mapper.toDTO(shelfRepository.save(shelf));
    }

    @Override
    public ShelfV2DTO getById(String id) {
        return shelfV2Mapper.toDTO(getEntityById(id));
    }

    @Override
    public Optional<ShelfV2DTO> findById(String id) {
        return shelfRepository.findById(id)
                .map(shelfV2Mapper::toDTO);
    }

    @Override
    public Page<ShelfV2DTO> searchShelves(String ensignId, Pageable pageable) {
        var spec = ShelfV2Specification.buildSpecification(ensignId);
        return shelfRepository.findAll(spec, pageable)
                .map(shelfV2Mapper::toDTO);
    }

    @Override
    public List<ShelfV2DTO> findShelvesByEnsignId(String ensignId) {
        List<ShelfV2Entity> shelves = shelfRepository.findByEnsignId(ensignId);
        return shelves.stream().map(shelfV2Mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public ShelfV2DTO updateShelf(String id, UpdateShelfV2Form form, MultipartFile image) {
        log.info("[DefaultShelfV2Service.updateShelf] Mise à jour du rayon: {}", id);
        ShelfV2Entity shelf = getEntityById(id);

        if (form != null) {
            shelfV2Mapper.mapToEntity(form, shelf);
        }

        if (image != null) {
            uploadShelfImage(shelf, image);
        }

        return shelfV2Mapper.toDTO(shelfRepository.save(shelf));
    }

    @Override
    @Transactional
    public void updateRank(String currentShelfId, String referralShelfId) {
        ShelfV2Entity current = getEntityById(currentShelfId);
        var prevOrSupShelfFound = shelfRepository.findById(referralShelfId);

        prevOrSupShelfFound.ifPresent(shelfV2Entity -> {
            int prevOrSupRank = shelfV2Entity.getRank();
            shelfV2Entity.setRank(current.getRank());
            current.setRank(prevOrSupRank);
            shelfRepository.saveAll(List.of(current, shelfV2Entity));
        });
    }

    @Override
    @Transactional
    public void updateRank(String currentShelfId, int position) {
        ShelfV2Entity current = getEntityById(currentShelfId);
        ShelfV2Entity toChange = shelfRepository.findByRank(position).orElse(null);
        if (toChange == null || toChange.getRank() == 0) {
            current.setRank(position);
            shelfRepository.save(current);
        } else {
            int toChangeRank = toChange.getRank();
            toChange.setRank(current.getRank());
            current.setRank(toChangeRank);
            shelfRepository.saveAll(List.of(current, toChange));
        }
    }

    @Override
    @Transactional
    public void performAction(String shelfId, String action, String referralShelfId) {
        ShelfV2Entity shelf = getEntityById(shelfId);
        if (action.equals("rankUp") && referralShelfId != null && !referralShelfId.isEmpty()) {
            updateRank(shelfId, referralShelfId);
            return;
        }
        if (action.equals("rankDown") && referralShelfId != null && !referralShelfId.isEmpty()) {
            updateRank(shelfId, referralShelfId);
        }
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        ShelfV2Entity shelf = getEntityById(id);
        shelf.setDeletedAt(LocalDateTime.now());
        shelfRepository.save(shelf);
    }

    @Override
    @Transactional
    public void delete(ShelfV2Entity shelf) {
        log.info("Deleting shelf with id {}", shelf.getId());
        deleteById(shelf.getId());
    }

    private void uploadShelfImage(ShelfV2Entity shelf, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return;
        }
        String fileName = createNewFileName(image.getOriginalFilename());
        shelf.setImage(fileName);
        try {
            storageService.store(image, getShelfImageKey(fileName));
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde de l'image du rayon", e);
            throw new RuntimeException("Impossible de sauvegarder l'image", e);
        }
    }
}
