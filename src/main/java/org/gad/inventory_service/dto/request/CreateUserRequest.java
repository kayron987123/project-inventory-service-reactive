package org.gad.inventory_service.dto.request;

import lombok.Builder;

@Builder
public record CreateUserRequest(
        String name,
        String lastName,
        String username,
        String password,
        String email,
        String phone
) {
}
