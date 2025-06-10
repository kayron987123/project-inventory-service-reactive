package org.gad.inventory_service;

import org.gad.inventory_service.config.jwt.JwtUtils;
import org.gad.inventory_service.service.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {
    @Bean
    public JwtUtils jwtUtils() {
        return mock(JwtUtils.class);
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return mock(ReactiveAuthenticationManager.class);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }

    @Bean
    public BrandService brandService() {
        return Mockito.mock(BrandService.class);
    }

    @Bean
    public CategoryService categoryService() {
        return Mockito.mock(CategoryService.class);
    }

    @Bean
    public PermissionService permissionService() {
        return Mockito.mock(PermissionService.class);
    }

    @Bean
    public ProductService productService() {
        return Mockito.mock(ProductService.class);
    }

    @Bean
    public ProviderService providerService() {
        return Mockito.mock(ProviderService.class);
    }

    @Bean
    public RoleService roleService() {
        return Mockito.mock(RoleService.class);
    }

    @Bean
    public SaleService saleService() {
        return Mockito.mock(SaleService.class);
    }

    @Bean
    public StocktakingService stocktakingService() {
        return Mockito.mock(StocktakingService.class);
    }

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }
}
