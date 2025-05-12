package org.gad.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(
        @NotBlank(message = "Category name cannot be empty")
        String name
) {
}
