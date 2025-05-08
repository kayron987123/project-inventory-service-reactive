package org.gad.inventory_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "stocktaking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Stocktaking {
    @Id
    private UUID idStocktaking;
    private Product product;
    private Integer quantity;
    private LocalDateTime stocktakingDate;
}
