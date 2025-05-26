package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "brands")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Brand {
    @Id
    private String idBrand;

    @Indexed(unique = true)
    @Field("name")
    private String name;

    @Builder.Default
    @Field("is_active")
    private boolean isActive = true;
}
