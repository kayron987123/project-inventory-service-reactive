package org.gad.inventory_service.dto;

import lombok.Builder;

@Builder
public record ProviderDTO(
        String uuidProvider,
        String name,
        String ruc,
        String dni,
        String address,
        String phone,
        String email
) {
}
