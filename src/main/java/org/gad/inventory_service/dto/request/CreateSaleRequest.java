package org.gad.inventory_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateSaleRequest(
        @JsonProperty("name_product")
        String nameProduct,
        Integer quantity
) {
}
