package org.marketplace_lea.common.common.exceptions;

public class InsufficientBalanceException extends ApplicationException {
    public InsufficientBalanceException() {
        super("Le montant de votre balance est insuffisant !");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
