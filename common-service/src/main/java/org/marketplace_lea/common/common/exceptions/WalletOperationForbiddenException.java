package org.marketplace_lea.common.common.exceptions;

import org.springframework.http.HttpStatus;


public class WalletOperationForbiddenException extends ConsoEpargneException {
    public WalletOperationForbiddenException() {
        super("Cette Opération est interdite !", HttpStatus.BAD_REQUEST);
    }

    public WalletOperationForbiddenException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
