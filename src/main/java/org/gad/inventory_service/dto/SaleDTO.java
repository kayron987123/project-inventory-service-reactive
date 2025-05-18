package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SaleDTO(
        @JsonProperty("uuid_sale")
        String uuidSale,

        @JsonProperty("name_product")
        String nameProduct,

        @JsonProperty("sale_date")
        String saleDate,

        Integer quantity,

        @JsonProperty("total_price")
        BigDecimal totalPrice
) {
}
