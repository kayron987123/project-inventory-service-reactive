package org.gad.inventory_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateCategoryRequest(
        @NotBlank(message = "Category name cannot be empty")
        String name
) {
}
