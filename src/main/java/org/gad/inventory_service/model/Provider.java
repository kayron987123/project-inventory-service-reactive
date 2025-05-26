package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "providers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Provider {
    @Id
    private String idProvider;

    @Field("name")
    private String name;

    @Field("ruc")
    @Indexed(unique = true, sparse = true)
    private String ruc;

    @Field("dni")
    @Indexed(unique = true, sparse = true)
    private String dni;

    @Field("address")
    private String address;

    @Field("phone")
    private String phone;

    @Field("email")
    @Indexed(unique = true)
    private String email;

    @Builder.Default
    @Field("is_active")
    private boolean isActive = true;
}
