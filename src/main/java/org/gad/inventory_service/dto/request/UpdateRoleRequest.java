package org.gad.inventory_service.dto.request;

import java.util.Set;

public record UpdateRoleRequest(
        String name,
        Set<String> permissions
) {
}
