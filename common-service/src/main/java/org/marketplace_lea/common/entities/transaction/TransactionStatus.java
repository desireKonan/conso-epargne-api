package org.marketplace_lea.common.entities.transaction;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    SUCCESS("Validée"), PENDING("En attente"), FAILED("Echouée"), IN_PROGRESS("En cours");

    private final String label;

    TransactionStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
