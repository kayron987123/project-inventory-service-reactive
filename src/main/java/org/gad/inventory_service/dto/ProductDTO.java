package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDTO(
        @JsonProperty("id_product")
        String idProduct,
        String name,
        String description,
        BigDecimal price,
        @JsonProperty("category_name")
        String categoryName,
        @JsonProperty("brand_name")
        String brandName,
        @JsonProperty("provider_name")
        String providerName,
        @JsonProperty("is_active")
        boolean isActive,
        @JsonProperty("created_at")
        String createdAt,
        @JsonProperty("updated_at")
        String updatedAt
) {
}
