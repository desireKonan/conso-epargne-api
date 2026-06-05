package org.marketplace_lea.common.common.exceptions;

import org.springframework.http.HttpStatus;

public class AmountCannotBeNegative2Exception extends ConsoEpargneException {

    public AmountCannotBeNegative2Exception() {
        super("Entrez un montant strictement supérieur à zéro(0)", HttpStatus.BAD_REQUEST);
    }

    public AmountCannotBeNegative2Exception(String message) {
        super(message);
    }

}
