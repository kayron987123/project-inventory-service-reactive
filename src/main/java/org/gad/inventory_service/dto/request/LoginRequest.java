package org.gad.inventory_service.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
