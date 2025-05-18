package org.gad.inventory_service.exception;

public class InvalidPriceRangeException extends RuntimeException {
    public InvalidPriceRangeException(String message) {
        super(message);
    }

    public InvalidPriceRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
