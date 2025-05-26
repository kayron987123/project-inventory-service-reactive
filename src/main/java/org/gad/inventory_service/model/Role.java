package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document(collection = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Role {
    @Id
    private String idRole;

    @Field("name")
    private String name;

    @Field("permissions")
    private Set<Permission> permissions;

    @Builder.Default
    @Field("is_active")
    private boolean isActive = true;
}
