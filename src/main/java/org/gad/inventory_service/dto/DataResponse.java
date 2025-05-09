package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public record DataResponse(
        int status,
        String message,
        Object data,
        String timestamp
) {
}
