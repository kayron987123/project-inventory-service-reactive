package org.gad.inventory_service.utils;

import org.gad.inventory_service.dto.ProductDTO;
import org.gad.inventory_service.model.Product;

public class Mappers {
    private Mappers() {
    }

    public static ProductDTO toDTO(Product product) {
        if (product == null) return null;
        return new ProductDTO(product.getIdProduct().toString(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getName(),
                product.getBrand().getName(),
                product.getProvider().getName());
    }
}
