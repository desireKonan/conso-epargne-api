package org.marketplace_lea.common.common.exceptions;

import org.springframework.http.HttpStatus;

public class ConsoWalletException extends ConsoEpargneException {
    public ConsoWalletException() {
        super("Wallet non trouvé !", HttpStatus.NOT_FOUND);
    }

    public ConsoWalletException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ConsoWalletException(String message, Throwable cause) {
        super(message, cause);
    }
}
