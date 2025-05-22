package org.gad.inventory_service.exception;

public class ExcelReportGenerationException extends RuntimeException {
    public ExcelReportGenerationException(String message) {
        super(message);
    }

    public ExcelReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
