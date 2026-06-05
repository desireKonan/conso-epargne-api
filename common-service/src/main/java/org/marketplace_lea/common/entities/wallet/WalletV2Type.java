package org.marketplace_lea.common.entities.wallet;

import java.util.Arrays;

public enum WalletV2Type {
    PERSONAL("PERSONAL"),
    LEA("LEA"),
    COLLECT("COLLECT"),
    INVESTMENT("INVESTMENT"),
    CONSOM("CONSOM");

    private final String type;

    WalletV2Type(String type) {
        this.type = type;
    }

    public WalletV2Type retrieveType(String type) {
        return Arrays.stream(values())
                .filter(value -> value.type.equals(type))
                .findFirst()
                .orElse(PERSONAL);
    }
}
