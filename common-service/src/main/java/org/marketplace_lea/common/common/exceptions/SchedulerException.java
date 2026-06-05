package org.marketplace_lea.common.common.exceptions;

/**
 * Exception spécifique pour les erreurs de scheduler
 */
public class SchedulerException extends RuntimeException {
    
    public SchedulerException(String message) {
        super(message);
    }
    
    public SchedulerException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SchedulerException(Throwable cause) {
        super(cause);
    }
}