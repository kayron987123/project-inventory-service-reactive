package org.gad.inventory_service.exception;

public class ProviderAlreadyExistsException extends RuntimeException {
    public ProviderAlreadyExistsException(String message) {
        super(message);
    }

    public ProviderAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
