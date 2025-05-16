package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = "brands")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Brand {
    @Id
    @Field("id_brand")
    private UUID idBrand;
    private String name;
}
