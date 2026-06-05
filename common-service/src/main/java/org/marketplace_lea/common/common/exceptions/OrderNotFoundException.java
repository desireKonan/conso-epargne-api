package org.marketplace_lea.common.common.exceptions;

public class OrderNotFoundException extends DataNotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException() {
        super("Commande non trouvée !");
    }
}
