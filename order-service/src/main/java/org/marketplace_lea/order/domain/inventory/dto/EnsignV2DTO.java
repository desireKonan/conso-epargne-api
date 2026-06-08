package org.marketplace_lea.order.domain.inventory.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnsignV2DTO {
    private String id;
    private String label;
    private String description;
    private int rank;
    private String image;
    private String imageUrl;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
