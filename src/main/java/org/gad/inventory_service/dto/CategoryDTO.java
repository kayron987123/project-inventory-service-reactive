package org.gad.inventory_service.dto;

import lombok.Builder;

@Builder
public record CategoryDTO(
        String uuid,
        String name
) {
}
