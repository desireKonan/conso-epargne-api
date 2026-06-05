package org.marketplace_lea.common.entities.collect;

public enum CollectStatus {
    PENDING("En attente"),
    VALIDATED("Validée"),
    CANCELED("Annulée"),
    STARTED("Démarée"),
    FINISHED("Terminée");

    private final String label;

    CollectStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
