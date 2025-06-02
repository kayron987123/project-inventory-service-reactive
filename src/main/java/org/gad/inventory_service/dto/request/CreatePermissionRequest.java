package org.gad.inventory_service.dto.request;

import lombok.Builder;

@Builder
public record CreatePermissionRequest(
        String name
) {
}
