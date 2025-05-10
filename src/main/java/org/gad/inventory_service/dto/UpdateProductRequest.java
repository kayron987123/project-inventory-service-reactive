package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "Description cannot be empty")
        String description,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        @DecimalMax(value = "99999.99", message = "Price must be less than 100000")
        @Digits(integer = 10, fraction = 2, message = "Price must be a valid decimal number with up to 2 decimal places")
        BigDecimal price,

        @JsonProperty("category_name")
        @NotBlank(message = "Category name cannot be empty")
        String categoryName,

        @JsonProperty("brand_name")
        @NotBlank(message = "Brand name cannot be empty")
        String brandName,

        @JsonProperty("provider_name")
        @NotBlank(message = "Provider name cannot be empty")
        String providerName
) {
}
