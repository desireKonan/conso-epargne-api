package org.marketplace_lea.common.common.exceptions;

public class NoNegativeAmountException extends RuntimeException {

    public NoNegativeAmountException() {
        super("Entrez un montant strictement supérieur à zéro(0)");
    }

    public NoNegativeAmountException(String message) {
        super(message);
    }

}
