package org.gad.inventory_service.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
        int status,
        String message,
        Object errors,
        String timestamp,
        String path
) {
}
