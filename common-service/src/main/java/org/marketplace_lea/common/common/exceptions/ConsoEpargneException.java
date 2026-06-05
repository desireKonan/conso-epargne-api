package org.marketplace_lea.common.common.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ConsoEpargneException extends RuntimeException {
    private HttpStatus status;

    public ConsoEpargneException() {
    }

    public ConsoEpargneException(String message) {
        super(message);
    }

    public ConsoEpargneException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ConsoEpargneException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsoEpargneException(Throwable cause) {
        super(cause);
    }
}
