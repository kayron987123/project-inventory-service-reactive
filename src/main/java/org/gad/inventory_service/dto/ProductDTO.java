package org.gad.inventory_service.dto;

import java.math.BigDecimal;

public record ProductDTO(
        String idProduct,
        String name,
        String description,
        BigDecimal price,
        String categoryName,
        String brandName,
        String providerName
) {
}
