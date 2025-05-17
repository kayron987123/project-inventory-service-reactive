package org.gad.inventory_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateStocktakingRequest(
        @JsonProperty("product_name")
        @NotBlank(message = "Product name cannot be blank")
        String productName,

        @NotNull(message = "Quantity cannot be null")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Positive(message = "Quantity must be positive")
        Integer quantity
) {
}
