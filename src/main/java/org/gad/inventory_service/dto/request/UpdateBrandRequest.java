package org.gad.inventory_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdateBrandRequest(
        @JsonProperty("brand_name")
        @NotBlank(message = "Brand name cannot be empty")
        String brandName
) {
}
