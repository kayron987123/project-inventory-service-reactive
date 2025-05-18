package org.gad.inventory_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateSaleRequest(
        @JsonProperty("name_product")
        String nameProduct,
        Integer quantity
) {
}
