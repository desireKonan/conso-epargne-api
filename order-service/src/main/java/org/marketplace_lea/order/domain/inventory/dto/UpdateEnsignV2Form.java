package org.marketplace_lea.order.domain.inventory.dto;

public record UpdateEnsignV2Form(
    String label,
    String description,
    Boolean available
) {}
