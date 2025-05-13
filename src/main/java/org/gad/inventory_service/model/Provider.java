package org.gad.inventory_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "providers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Provider {
    @Id
    private UUID idProvider;
    private String name;
    private String ruc;
    private String dni;
    private String address;
    private String phone;
    private String email;
}
