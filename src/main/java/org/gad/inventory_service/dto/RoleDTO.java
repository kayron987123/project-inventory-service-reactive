package org.gad.inventory_service.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record RoleDTO(
        String idRole,
        String name,
        Set<String> permissions
) {
}
