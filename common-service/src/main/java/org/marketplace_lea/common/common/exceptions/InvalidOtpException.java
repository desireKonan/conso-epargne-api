package org.marketplace_lea.common.common.exceptions;

public class InvalidOtpException extends ApplicationException {
    public InvalidOtpException() {
        super("OTP invalide");
    }

    public InvalidOtpException(String message) {
        super(message);
    }
}
