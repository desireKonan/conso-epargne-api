package org.marketplace_lea.order.domain.inventory.services;

import org.marketplace_lea.order.common.entities.inventory.ProductV2Entity;
import org.marketplace_lea.order.domain.inventory.dto.CreateProductV2Form;
import org.marketplace_lea.order.domain.inventory.dto.ProductV2Criteria;
import org.marketplace_lea.order.domain.inventory.dto.ProductV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateProductV2Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ProductV2Service {
    
    ProductV2DTO getById(String id);
    
    Optional<ProductV2DTO> findById(String id);

    Page<ProductV2DTO> searchProducts(ProductV2Criteria criteria, Pageable pageable);

    ProductV2DTO createProduct(CreateProductV2Form form, MultipartFile image, MultipartFile[] gallery);

    ProductV2DTO updateProduct(String id, UpdateProductV2Form form, MultipartFile image);

    void updateRank(String currentProductId, String referralProductId);

    void updateRank(String currentProductId, int position);

    void performAction(String productId, String action, String referralProductId);

    void deleteById(String id);

    void delete(ProductV2Entity product);

    void changeRank(String productId, int newRank);
}

