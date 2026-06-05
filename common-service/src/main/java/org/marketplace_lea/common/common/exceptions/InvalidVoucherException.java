package org.marketplace_lea.common.common.exceptions;

public class InvalidVoucherException extends ApplicationException {
    public InvalidVoucherException() {
        super("Bon de réduction invalide !");
    }
}
