package org.marketplace_lea.common.common.exceptions;

public class CustomerNotFoundException extends DataNotFoundException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException() {
        super("Client non trouvé");
    }
}
