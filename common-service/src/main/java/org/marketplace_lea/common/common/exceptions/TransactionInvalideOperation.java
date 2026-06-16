package org.marketplace_lea.common.common.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionInvalideOperation extends ConsoEpargneException {

    public TransactionInvalideOperation(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
