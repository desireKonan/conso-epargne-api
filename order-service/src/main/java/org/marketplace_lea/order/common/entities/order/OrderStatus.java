package org.marketplace_lea.order.common.entities.order;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {
    PENDING("pending"), VALIDATED("validated"), CANCELED("canceled");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return switch (this) {
            case CANCELED -> "Annulé";
            case PENDING -> "En attente";
            case VALIDATED -> "Validé";
            default -> "Aucun status !";
        };
    }


    public static Optional<OrderStatus> getByStatus(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.label.equals(status))
                .findAny();
    }

    @Override
    public String toString() {
        return this.label;
    }
}
