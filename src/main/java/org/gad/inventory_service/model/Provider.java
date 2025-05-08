package org.gad.inventory_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "providers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Provider {
    @Id
    private UUID idProvider;
    private String name;
    private String dni;
    private String address;
    private String phone;
    private String email;
}
