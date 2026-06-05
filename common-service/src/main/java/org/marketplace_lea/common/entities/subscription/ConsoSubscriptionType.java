package org.marketplace_lea.common.entities.subscription;

import java.math.BigDecimal;

public enum ConsoSubscriptionType {
    SILVER("ARGENT", 25_000),
    OR("OR", 50_000),

    /// For Solidary Funds it's really differents, It's an multiplier.
    SOLIDARITY_FUND("SOLIDARITY_FUND", 2);

    private final String value;
    private final int amount;

    ConsoSubscriptionType(String value, int amount) {
        this.value = value;
        this.amount = amount;
    }

    public String value() {
        return this.value;
    }


    public BigDecimal amount() {
        return  BigDecimal.valueOf(this.amount);
    }
}
