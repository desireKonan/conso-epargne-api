package org.marketplace_lea.common.common.exceptions;

public class AmountCannotBeNegativeException extends ApplicationException {

    public AmountCannotBeNegativeException() {
        super("Entrez un montant strictement supérieur à zéro(0)");
    }

    public AmountCannotBeNegativeException(String message) {
        super(message);
    }

}
