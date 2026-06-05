package org.marketplace_lea.common.common.exceptions;

public class QuantityNotValidException extends ApplicationException {
    public QuantityNotValidException() {
        super("Quantité non valide.");
    }

    public QuantityNotValidException(String message) {
        super(message);
    }
}
