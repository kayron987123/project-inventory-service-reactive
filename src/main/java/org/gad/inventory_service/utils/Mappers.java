package org.gad.inventory_service.utils;

import org.gad.inventory_service.dto.*;
import org.gad.inventory_service.model.*;

import static org.gad.inventory_service.utils.UtilsMethods.convertUUIDToString;
import static org.gad.inventory_service.utils.UtilsMethods.localDateTimeFormatted;

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

    public static StocktakingDTO stocktakingToDTO(Stocktaking stocktaking) {
        if (stocktaking == null) return null;
        return StocktakingDTO.builder()
                .uuidStocktaking(convertUUIDToString(stocktaking.getIdStocktaking()))
                .productName(stocktaking.getProduct().getName())
                .quantity(stocktaking.getQuantity())
                .stocktakingDate(localDateTimeFormatted(stocktaking.getStocktakingDate()))
                .build();
    }

    public static SaleDTO saleToDTO(Sale sale) {
        if (sale == null) return null;
        return SaleDTO.builder()
                .uuidSale(convertUUIDToString(sale.getIdSale()))
                .nameProduct(sale.getProduct().getName())
                .saleDate(localDateTimeFormatted(sale.getSaleDate()))
                .quantity(sale.getQuantity())
                .totalPrice(sale.getTotalPrice())
                .build();
    }
}
