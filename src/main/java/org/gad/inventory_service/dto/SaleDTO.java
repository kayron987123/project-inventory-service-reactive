package org.gad.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SaleDTO(
        @JsonProperty("id_sale")
        String idSale,

        @JsonProperty("name_product")
        String nameProduct,

        Integer quantity,

        @JsonProperty("total_price")
        BigDecimal totalPrice,

        @JsonProperty("sale_date")
        String saleDate
) {
}
