package org.marketplace_lea.common.common.exceptions;

public class InsufficientFundException extends ApplicationException {
    public InsufficientFundException() {
        super("Fonds insuffisants pour éffectuer la transaction.");
    }

    public InsufficientFundException(String message) {
        super(message);
    }

}
