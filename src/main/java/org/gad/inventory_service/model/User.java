package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"password"})
public class User {
    @Id
    private String idUser;

    @Field("name")
    private String name;

    @Field("last_name")
    private String lastName;

    @Indexed(unique = true)
    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("email")
    @Indexed(unique = true, sparse = true)
    private String email;

    @Field("phone")
    private String phone;

    @Field("roles")
    private Set<Role> roles;

    @Builder.Default
    @Field("is_active")
    private Boolean isActive = true;

    @Builder.Default
    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("updated_at")
    private LocalDateTime updatedAt;

    public void updateTimeStamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
