package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ProviderDTO(
        @JsonProperty("id_provider")
        String idProvider,
        String name,
        String address,
        String phone,
        @JsonProperty("is_active")
        boolean isActive
) {
}
