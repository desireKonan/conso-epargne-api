package org.marketplace_lea.order.domain.inventory.dto;

import lombok.Data;
import org.marketplace_lea.order.common.entities.inventory.ProductType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProductV2DTO {
    private String id;
    private String label;
    private String description;
    private int rank;
    private double weight;
    private String brand;
    private BigDecimal purchasePrice;
    private BigDecimal consoEpargneMargin;
    private BigDecimal comissionAccountAmount;
    private BigDecimal price;
    private BigDecimal savingAmount;
    private ProductType type;
    private boolean soldOut;
    private boolean activeCashback;
    private String imageUrl;
    private Set<String> images;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Flattened fields
    private String shelfId;
    private String shelfName;
    private String ensignId;
}
