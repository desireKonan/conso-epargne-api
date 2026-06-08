package org.marketplace_lea.order.domain.inventory.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShelfV2DTO {
    private String id;
    private String label;
    private String description;
    private int rank;
    private String image;
    private String imageUrl;
    private String parentId;
    private String parentLabel;
    private String ensignId;
    private String ensignLabel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
