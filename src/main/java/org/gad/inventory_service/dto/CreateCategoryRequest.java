package org.gad.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name cannot be empty")
        String name
) {
}
