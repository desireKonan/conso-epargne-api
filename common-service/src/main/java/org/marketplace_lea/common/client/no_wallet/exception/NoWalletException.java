package org.marketplace_lea.common.client.no_wallet.exception;

import org.springframework.http.HttpStatus;

public class NoWalletException extends RuntimeException {
    private HttpStatus status;

    public NoWalletException(String message) {
        super(message);
    }

    public NoWalletException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public NoWalletException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public HttpStatus getStatus() {
        return status;
    }
}