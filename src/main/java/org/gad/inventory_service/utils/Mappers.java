package org.gad.inventory_service.utils;

import org.gad.inventory_service.dto.BrandDTO;
import org.gad.inventory_service.dto.CategoryDTO;
import org.gad.inventory_service.dto.ProductDTO;
import org.gad.inventory_service.model.Brand;
import org.gad.inventory_service.model.Category;
import org.gad.inventory_service.model.Product;

import static org.gad.inventory_service.utils.UtilsMethods.convertUUIDToString;

public class Mappers {
    private Mappers() {
    }

    public static ProductDTO toDTO(Product product) {
        if (product == null) return null;
        return ProductDTO.builder()
                .idProduct(convertUUIDToString(product.getIdProduct()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryName(product.getCategory().getName())
                .brandName(product.getBrand().getName())
                .providerName(product.getProvider().getName())
                .build();
    }

    public static BrandDTO toDTO(Brand brand) {
        if (brand == null) return null;
        return BrandDTO.builder()
                .uuid(convertUUIDToString(brand.getIdBrand()))
                .name(brand.getName())
                .build();
    }

    public static CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        return CategoryDTO.builder()
                .uuid(convertUUIDToString(category.getIdCategory()))
                .name(category.getName())
                .build();
    }
}
