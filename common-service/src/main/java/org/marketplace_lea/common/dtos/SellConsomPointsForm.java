package org.marketplace_lea.common.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// ID du compte vendeur
// Montant à vendre
public record SellConsomPointsForm(
        @NotNull
        String accountId,
        @Min(1)
        @NotNull
        BigDecimal amount,
        String numberAndOperator
) {
}

