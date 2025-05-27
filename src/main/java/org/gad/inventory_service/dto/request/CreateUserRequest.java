package org.gad.inventory_service.dto.request;

public record CreateUserRequest(
        String name,
        String lastName,
        String username,
        String password,
        String email,
        String phone
) {
}
