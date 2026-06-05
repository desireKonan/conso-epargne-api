package org.marketplace_lea.common.common.exceptions;

public class TransactionPendingException extends ApplicationException {
    public TransactionPendingException() {
        super("Vous avez une transaction en cours ! ");
    }
}
