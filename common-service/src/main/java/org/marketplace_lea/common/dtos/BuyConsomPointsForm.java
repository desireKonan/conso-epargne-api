package org.marketplace_lea.common.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BuyConsomPointsForm(
        @NotNull
        String buyerAccountId,
        @NotNull
        String sellerAccountId,
        @Min(1)
        @NotNull
        BigDecimal amount
) {
}
