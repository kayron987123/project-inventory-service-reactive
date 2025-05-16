package org.gad.inventory_service.dto.response;

import lombok.Builder;

@Builder
public record DataResponse(
        int status,
        String message,
        Object data,
        String timestamp
) {
}
