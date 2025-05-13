package org.gad.inventory_service.dto;

public record CreateProviderRequest(
        String name,
        String ruc,
        String dni,
        String address,
        String phone,
        String email
) {
}
