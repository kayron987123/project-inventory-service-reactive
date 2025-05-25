package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = "permissions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Permission {
    @Id
    @Field("id_permission")
    private UUID idPermission;
    private String name;
}
