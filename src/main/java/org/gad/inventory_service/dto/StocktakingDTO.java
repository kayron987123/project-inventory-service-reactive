package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record StocktakingDTO(
        @JsonProperty("uuid_stocktaking")
        String uuidStocktaking,
        @JsonProperty("product_name")
        String productName,
        Integer quantity,
        @JsonProperty("stocktaking_date")
        String stocktakingDate
) {
}
