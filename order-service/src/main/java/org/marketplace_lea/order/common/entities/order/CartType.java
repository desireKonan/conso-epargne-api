package org.marketplace_lea.order.common.entities.order;

import java.util.Arrays;

public enum CartType {
    CART("CART"),
    WISH_LIST("WISH_LIST");

    private final String type;

    CartType(String type) {
        this.type = type;
    }

    public CartType retrieveType(String type) {
        return Arrays.stream(values())
                .filter(value -> value.type.equals(type))
                .findFirst()
                .orElse(CART);
    }
}
