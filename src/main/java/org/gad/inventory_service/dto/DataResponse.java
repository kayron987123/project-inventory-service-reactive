package org.gad.inventory_service.dto;

import lombok.Builder;

@Builder
public record DataResponse(
        int status,
        String message,
        Object data,
        String timestamp
) {
}
