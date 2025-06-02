package org.gad.inventory_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UpdateProviderRequest(
        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "RUC cannot be empty")
        @Pattern(regexp = "^\\d{11}$", message = "RUC must be 11 digits")
        String ruc,

        @NotBlank(message = "DNI cannot be empty")
        @Pattern(regexp = "^\\d{8}$", message = "DNI must be 8 digits")
        String dni,

        @NotBlank(message = "Address cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9\\s,.-]+$", message = "Address can only contain letters, numbers, spaces, and special characters (,.-)")
        String address,

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^\\d{9}$", message = "Phone must be 9 digits")
        String phone,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email
) {
}
