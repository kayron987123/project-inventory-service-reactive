package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CategoryDTO(
        @JsonProperty("id_category")
        String idCategory,
        String name,
        @JsonProperty("is_active")
        boolean isActive
) {
}
