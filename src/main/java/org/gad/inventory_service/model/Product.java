package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.UUID;

@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @Field("id_product")
    private UUID idProduct;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private Brand brand;
    private Provider provider;
}
