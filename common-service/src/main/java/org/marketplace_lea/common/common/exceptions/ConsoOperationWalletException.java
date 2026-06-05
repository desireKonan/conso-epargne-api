package org.marketplace_lea.common.common.exceptions;

import org.springframework.http.HttpStatus;

public class ConsoOperationWalletException extends ConsoEpargneException {
    public ConsoOperationWalletException() {
        super("Opération non possible sur le wallet !", HttpStatus.BAD_REQUEST);
    }

    public ConsoOperationWalletException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public ConsoOperationWalletException(String message, Throwable cause) {
        super(message, cause);
    }
}
