package org.gad.inventory_service.exception;

public class SalesNotFoundException extends RuntimeException {
    public SalesNotFoundException(String message) {
        super(message);
    }

    public SalesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
