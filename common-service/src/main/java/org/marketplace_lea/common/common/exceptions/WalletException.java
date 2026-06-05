package org.marketplace_lea.common.common.exceptions;

public class WalletException extends ApplicationException {
    public WalletException() {
    }

    public WalletException(String message) {
        super(message);
    }

    public WalletException(String message, Throwable cause) {
        super(message, cause);
    }
}
