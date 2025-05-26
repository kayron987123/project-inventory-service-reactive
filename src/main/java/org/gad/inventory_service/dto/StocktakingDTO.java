package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record StocktakingDTO(
        @JsonProperty("id_stocktaking")
        String idStocktaking,
        @JsonProperty("product_name")
        String productName,
        Integer quantity,
        @JsonProperty("stocktaking_date")
        String stocktakingDate,
        @JsonProperty("performed_by")
        String performedBy
) {
}
