package org.marketplace_lea.common.client.paystack.exception;

import org.springframework.http.HttpStatus;

public class PaystackException extends RuntimeException {
    private HttpStatus status;
    
    public PaystackException(String message) {
        super(message);
    }
    
    public PaystackException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
    public PaystackException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public HttpStatus getStatus() {
        return status;
    }
}