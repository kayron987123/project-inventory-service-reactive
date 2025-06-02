package org.gad.inventory_service.dto.request;

import lombok.Builder;

import java.util.Set;

@Builder
public record CreateRoleRequest(
        String name,
        Set<String> permissions
) {
}
