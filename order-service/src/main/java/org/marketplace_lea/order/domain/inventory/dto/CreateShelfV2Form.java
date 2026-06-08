package org.marketplace_lea.order.domain.inventory.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateShelfV2Form(
    @NotBlank(message = "Le label est obligatoire") String label,
    @NotBlank(message = "La description est obligatoire") String description,
    String parentId,
    @NotBlank(message = "L'ID de l'enseigne est obligatoire") String ensignId
) {}
