package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "stocktaking")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Stocktaking {
    @Id
    @Field("id_stocktaking")
    private UUID idStocktaking;
    private Product product;
    private Integer quantity;
    @Field("stocktaking_date")
    private LocalDateTime stocktakingDate;
}
