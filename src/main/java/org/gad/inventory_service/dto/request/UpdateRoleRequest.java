package org.gad.inventory_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.Set;

@Builder
public record UpdateRoleRequest(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotEmpty(message = "Permissions cannot be empty")
        Set<@Pattern(regexp = "^[a-zA-Z]+$", message = "Only allowed letters") String> permissions
) {
}
