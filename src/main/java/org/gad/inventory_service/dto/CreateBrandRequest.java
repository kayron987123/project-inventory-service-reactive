package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateBrandRequest(
        @JsonProperty("brand_name")
        @NotBlank(message = "Brand name cannot be empty")
        String brandName
) {
}
