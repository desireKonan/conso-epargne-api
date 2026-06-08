package org.marketplace_lea.order.domain.inventory.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateEnsignV2Form(
    @NotBlank(message = "Le label est obligatoire") String label,
    @NotBlank(message = "La description est obligatoire") String description,
    boolean available
) {}
