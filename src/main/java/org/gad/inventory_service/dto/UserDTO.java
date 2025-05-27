package org.gad.inventory_service.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        String idUser,
        String name,
        String lastName,
        String phone,
        String createdAt,
        String updatedAt
) {
}
