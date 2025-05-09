package org.gad.inventory_service.dto;

public record ErrorResponse(
        int status,
        String message,
        Object errors,
        String timestamp,
        String path
) {
}
