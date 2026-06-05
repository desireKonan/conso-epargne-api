package org.marketplace_lea.common.common.exceptions;

public class DataNotFoundException extends ApplicationException {
    public DataNotFoundException() {
        super("Donnée non trouvée");
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
