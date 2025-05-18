package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "sales")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Sale {
    @Id
    @Field("id_sale")
    private UUID idSale;
    private Product product;
    @Field("sale_date")
    private LocalDateTime saleDate;
    private Integer quantity;
    @Field("total_price")
    private BigDecimal totalPrice;
}
