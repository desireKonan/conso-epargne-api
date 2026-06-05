package org.marketplace_lea.common.common.exceptions;

public class CollectNotFoundException extends DataNotFoundException {

    public CollectNotFoundException() {
        super("Collecte non trouvée !");
    }

    public CollectNotFoundException(String message) {
        super(message);
    }
}
