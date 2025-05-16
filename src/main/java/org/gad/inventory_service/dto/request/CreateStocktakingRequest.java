package org.gad.inventory_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateStocktakingRequest(
        @JsonProperty("product_name")
        String productName,
        Integer quantity
) {
}
