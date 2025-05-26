package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "permissions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Permission{
    @Id
    private String idPermission;

    @Field("name")
    private String name;

    @Builder.Default
    @Field("is_active")
    private Boolean isActive = true;
}
