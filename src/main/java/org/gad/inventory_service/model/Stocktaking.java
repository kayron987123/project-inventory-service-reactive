package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "stocktaking")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Stocktaking {
    @Id
    private String idStocktaking;

    @Field("product_id")
    private String productId;

    @Field("quantity")
    private Integer quantity;

    @Builder.Default
    @Field("stocktaking_date")
    private LocalDateTime stocktakingDate = LocalDateTime.now();

    @Field("performed_by")
    private String performedBy;
}
