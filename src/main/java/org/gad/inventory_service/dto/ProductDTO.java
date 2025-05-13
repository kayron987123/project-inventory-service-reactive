package org.gad.inventory_service.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDTO(
        String uuidProduct,
        String name,
        String description,
        BigDecimal price,
        String categoryName,
        String brandName,
        String providerName
) {
}
