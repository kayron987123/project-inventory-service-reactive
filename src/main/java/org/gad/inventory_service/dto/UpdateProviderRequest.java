package org.gad.inventory_service.dto;

public record UpdateProviderRequest(
        String name,
        String ruc,
        String dni,
        String address,
        String phone,
        String email
) {
}
