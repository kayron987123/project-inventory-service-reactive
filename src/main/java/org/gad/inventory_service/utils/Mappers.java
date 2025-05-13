package org.gad.inventory_service.utils;

import org.gad.inventory_service.dto.BrandDTO;
import org.gad.inventory_service.dto.CategoryDTO;
import org.gad.inventory_service.dto.ProductDTO;
import org.gad.inventory_service.dto.ProviderDTO;
import org.gad.inventory_service.model.Brand;
import org.gad.inventory_service.model.Category;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.model.Provider;

import static org.gad.inventory_service.utils.UtilsMethods.convertUUIDToString;

public class Mappers {
    private Mappers() {
    }

    public static ProductDTO productToDTO(Product product) {
        if (product == null) return null;
        return ProductDTO.builder()
                .uuidProduct(convertUUIDToString(product.getIdProduct()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryName(product.getCategory().getName())
                .brandName(product.getBrand().getName())
                .providerName(product.getProvider().getName())
                .build();
    }

    public static BrandDTO brandToDTO(Brand brand) {
        if (brand == null) return null;
        return BrandDTO.builder()
                .uuidBrand(convertUUIDToString(brand.getIdBrand()))
                .name(brand.getName())
                .build();
    }

    public static CategoryDTO categoryToDTO(Category category) {
        if (category == null) return null;
        return CategoryDTO.builder()
                .uuidCategory(convertUUIDToString(category.getIdCategory()))
                .name(category.getName())
                .build();
    }

    public static ProviderDTO providerToDTO(Provider provider) {
        if (provider == null) return null;
        return ProviderDTO.builder()
                .uuidProvider(convertUUIDToString(provider.getIdProvider()))
                .name(provider.getName())
                .ruc(provider.getRuc())
                .dni(provider.getDni())
                .address(provider.getAddress())
                .phone(provider.getPhone())
                .email(provider.getEmail())
                .build();
    }
}
