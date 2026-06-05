package org.marketplace_lea.common.client.wane_delivery.exception;

import org.springframework.http.HttpStatus;

public class WaneDeliveryException extends RuntimeException {
    private HttpStatus status;

    public WaneDeliveryException(String message) {
        super(message);
    }

    public WaneDeliveryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public WaneDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public HttpStatus getStatus() {
        return status;
    }
}