package org.gad.inventory_service.exception;

public class StockTakingNotFoundException extends RuntimeException {
    public StockTakingNotFoundException(String message) {
        super(message);
    }

    public StockTakingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
