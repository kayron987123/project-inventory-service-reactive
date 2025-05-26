package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BrandDTO(
        @JsonProperty("id_brand")
        String idBrand,
        String name,
        @JsonProperty("is_active")
        boolean isActive
) {
}
