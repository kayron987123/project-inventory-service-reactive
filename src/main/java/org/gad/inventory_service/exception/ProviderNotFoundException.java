package org.gad.inventory_service.exception;

public class ProviderNotFoundException extends RuntimeException{
    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
