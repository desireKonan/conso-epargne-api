package org.marketplace_lea.order.domain.inventory.dto;

public record UpdateShelfV2Form(
    String label,
    String description,
    String parentId
) {}
