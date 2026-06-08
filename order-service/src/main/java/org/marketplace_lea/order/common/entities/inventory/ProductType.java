package org.marketplace_lea.order.common.entities.inventory;

import java.util.Arrays;

public enum ProductType {
    PRODUCT("PRODUCT"),
    ADHERANT_KIT("ADHERANT_KIT"),
    NONE("NONE");

    private final String type;

    ProductType(String type) {
        this.type = type;
    }

    public ProductType retrieveType(String type) {
        return Arrays.stream(values())
                .filter(value -> value.type.equals(type))
                .findFirst()
                .orElse(NONE);
    }
}
