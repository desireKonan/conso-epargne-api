package org.marketplace_lea.common.common.exceptions;

import org.springframework.http.HttpStatus;

public class ConsoEpargneNotFoundDataException extends ConsoEpargneException {
    public ConsoEpargneNotFoundDataException() {
        super("Donnée non trouvée !", HttpStatus.NOT_FOUND);
    }

    public ConsoEpargneNotFoundDataException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ConsoEpargneNotFoundDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
