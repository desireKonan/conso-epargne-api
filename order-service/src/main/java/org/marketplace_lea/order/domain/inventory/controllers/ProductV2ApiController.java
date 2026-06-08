package org.marketplace_lea.order.domain.inventory.controllers;

import org.marketplace_lea.order.domain.inventory.dto.CreateProductV2Form;
import org.marketplace_lea.order.domain.inventory.dto.ProductV2Criteria;
import org.marketplace_lea.order.domain.inventory.dto.ProductV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateProductV2Form;
import org.marketplace_lea.order.domain.inventory.services.ProductV2Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductV2ApiController {
    private final ProductV2Service productV2Service;

    @GetMapping
    public ResponseEntity<Page<ProductV2DTO>> searchProducts(
            @ModelAttribute ProductV2Criteria criteria,
            @PageableDefault(sort = "rank", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(productV2Service.searchProducts(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductV2DTO> getById(@PathVariable String id) {
        Optional<ProductV2DTO> product = productV2Service.findById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductV2DTO> createProduct(
            @Valid @RequestPart("data") CreateProductV2Form form,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "gallery", required = false) MultipartFile[] gallery) {
            
        ProductV2DTO created = productV2Service.createProduct(form, image, gallery);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductV2DTO> updateProduct(
            @PathVariable String id,
            @Valid @RequestPart("data") UpdateProductV2Form form,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(productV2Service.updateProduct(id, form, image));
    }

    @PatchMapping("/{id}/rank")
    public ResponseEntity<Void> updateRank(
            @PathVariable String id,
            @RequestParam(required = false) String referralProductId,
            @RequestParam(required = false) Integer position) {
        if (position != null) {
            productV2Service.updateRank(id, position);
        } else if (referralProductId != null) {
            productV2Service.updateRank(id, referralProductId);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/action")
    public ResponseEntity<Void> performAction(
            @PathVariable String id,
            @RequestParam String action,
            @RequestParam(required = false) String referralProductId) {
        productV2Service.performAction(id, action, referralProductId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/change-rank")
    public ResponseEntity<Void> changeRank(
            @PathVariable String id,
            @RequestParam int newRank) {
        productV2Service.changeRank(id, newRank);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productV2Service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
