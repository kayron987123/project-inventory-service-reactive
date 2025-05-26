package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "sales")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Sale {
    @Id
    private String idSale;

    @Field("product_id")
    private String productId;

    @Field("quantity")
    private Integer quantity;

    @Field("total_price")
    private BigDecimal totalPrice;

    @Builder.Default
    @Indexed
    @Field("sale_date")
    private LocalDateTime saleDate = LocalDateTime.now();
}
