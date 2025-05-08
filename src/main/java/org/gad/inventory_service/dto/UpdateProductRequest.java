package org.gad.inventory_service.dto;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String description,
        BigDecimal price,
        String categoryName,
        String brandName,
        String providerName
) {
}
