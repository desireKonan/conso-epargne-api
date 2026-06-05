package org.marketplace_lea.common.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ConsomWalletStatsDTO(
        @JsonProperty("total_balance")
        BigDecimal totalBalance,

        @JsonProperty("total_drawn")
        BigDecimal totalDrawn
) {
    public static ConsomWalletStatsDTO ZERO() {
        return new ConsomWalletStatsDTO(BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
