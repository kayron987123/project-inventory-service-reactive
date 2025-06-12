package org.gad.inventory_service.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserAuthenticatedDTO(
        String idUser,
        String name,
        String lastName,
        String username,
        String email,
        String phone,
        Set<String> roles
) {
}
