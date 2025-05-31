package org.gad.inventory_service.utils;

import org.gad.inventory_service.dto.*;
import org.gad.inventory_service.model.*;

import java.util.Set;
import java.util.stream.Collectors;

import static org.gad.inventory_service.utils.UtilsMethods.localDateTimeFormatted;

public class Mappers {
    private Mappers() {
    }

    public static ProductDTO productToDTO(Product product, String categoryName, String brandName, String providerName) {
        if (product == null) return null;
        return ProductDTO.builder()
                .idProduct(product.getIdProduct())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryName(categoryName)
                .brandName(brandName)
                .providerName(providerName)
                .isActive(product.isActive())
                .createdAt(localDateTimeFormatted(product.getCreatedAt()))
                .updatedAt(localDateTimeFormatted(product.getUpdatedAt()))
                .build();
    }

    public static BrandDTO brandToDTO(Brand brand) {
        if (brand == null) return null;
        return BrandDTO.builder()
                .idBrand(brand.getIdBrand())
                .name(brand.getName())
                .build();
    }

    public static CategoryDTO categoryToDTO(Category category) {
        if (category == null) return null;
        return CategoryDTO.builder()
                .idCategory(category.getIdCategory())
                .name(category.getName())
                .build();
    }

    public static ProviderDTO providerToDTO(Provider provider) {
        if (provider == null) return null;
        return ProviderDTO.builder()
                .idProvider(provider.getIdProvider())
                .name(provider.getName())
                .address(provider.getAddress())
                .phone(provider.getPhone())
                .isActive(provider.isActive())
                .build();
    }

    public static StocktakingDTO stocktakingToDTO(Stocktaking stocktaking, String productName) {
        if (stocktaking == null) return null;
        return StocktakingDTO.builder()
                .idStocktaking(stocktaking.getIdStocktaking())
                .productName(productName)
                .quantity(stocktaking.getQuantity())
                .stocktakingDate(localDateTimeFormatted(stocktaking.getStocktakingDate()))
                .performedBy(stocktaking.getPerformedBy())
                .build();
    }

    public static SaleDTO saleToDTO(Sale sale, String productName) {
        if (sale == null) return null;
        return SaleDTO.builder()
                .idSale(sale.getIdSale())
                .nameProduct(productName)
                .saleDate(localDateTimeFormatted(sale.getSaleDate()))
                .quantity(sale.getQuantity())
                .totalPrice(sale.getTotalPrice())
                .build();
    }

    public static UserDTO userToDTO(User user) {
        if (user == null) return null;
        return UserDTO.builder()
                .idUser(user.getIdUser())
                .name(user.getName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .createdAt(localDateTimeFormatted(user.getCreatedAt()))
                .updatedAt(localDateTimeFormatted(user.getUpdatedAt()))
                .build();
    }

    public static RoleDTO roleToDTO(Role role) {
        if (role == null) return null;
        return RoleDTO.builder()
                .idRole(role.getIdRole())
                .name(role.getName())
                .permissions(permissionsToSetString(role.getPermissions()))
                .build();
    }

    public static PermissionDTO permissionToDTO(Permission permission) {
        if (permission == null) return null;
        return PermissionDTO.builder()
                .idPermission(permission.getIdPermission())
                .name(permission.getName())
                .build();
    }

    private static Set<String> permissionsToSetString(Set<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) return Set.of();
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}
