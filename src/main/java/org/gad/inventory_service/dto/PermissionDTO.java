package org.gad.inventory_service.dto;

import lombok.Builder;

@Builder
public record PermissionDTO(
        String idPermission,
        String name
) {
}
