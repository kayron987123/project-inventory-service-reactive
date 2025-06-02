package org.gad.inventory_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateUserRequest(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotBlank(message = "Last name cannot be empty")
        String lastName,
        @NotBlank(message = "Username cannot be empty")
        String username,
        @NotBlank(message = "Password cannot be empty")
        String password,
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email,
        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^9\\d{8}$", message = "El teléfono debe tener 9 dígitos numéricos y comenzar con 9")
        String phone
) {
}
