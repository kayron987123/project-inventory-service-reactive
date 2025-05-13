package org.gad.inventory_service.dto;

import lombok.Builder;

@Builder
public record BrandDTO(
        String uuidBrand,
        String name
) {
}
