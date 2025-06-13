package org.gad.inventory_service.config.security;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.config.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.gad.inventory_service.utils.Constants.*;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public AuthTokenFilter authTokenFilter(JwtUtils jwtUtils,
                                           ReactiveUserDetailsService userDetailsService) {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         AuthTokenFilter authTokenFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, AUTH_LOGIN_URL).permitAll()

                        .pathMatchers(HttpMethod.GET, BRAND_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_ANALYST, ROLE_SUPPORT, ROLE_BASIC_USER)
                        .pathMatchers(HttpMethod.GET, CATEGORY_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_ANALYST, ROLE_SUPPORT, ROLE_BASIC_USER)
                        .pathMatchers(HttpMethod.GET, PRODUCT_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_SALESPERSON, ROLE_WAREHOUSE_STAFF, ROLE_ANALYST, ROLE_SUPPORT, ROLE_BASIC_USER)
                        .pathMatchers(HttpMethod.GET, PROVIDER_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_SALESPERSON, ROLE_WAREHOUSE_STAFF, ROLE_ANALYST, ROLE_SUPPORT, ROLE_BASIC_USER)
                        .pathMatchers(HttpMethod.GET, SALE_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_SALESPERSON, ROLE_ANALYST, ROLE_SUPPORT)
                        .pathMatchers(HttpMethod.GET, STOCKTAKING_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_WAREHOUSE_STAFF, ROLE_ANALYST, ROLE_SUPPORT)
                        .pathMatchers(HttpMethod.GET, ROLE_URL).hasAnyRole(ROLE_ADMIN, ROLE_ANALYST, ROLE_SUPPORT)
                        .pathMatchers(HttpMethod.GET, PERMISSION_URL).hasAnyRole(ROLE_ADMIN)
                        .pathMatchers(HttpMethod.GET, USER_URL).hasAnyRole(ROLE_ADMIN, ROLE_ANALYST,ROLE_SUPPORT)

                        .pathMatchers(HttpMethod.POST, BRAND_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.POST, CATEGORY_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.POST, PRODUCT_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.POST, PROVIDER_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.POST, SALE_URL).hasAnyRole(ROLE_ADMIN, ROLE_SALESPERSON)
                        .pathMatchers(HttpMethod.POST, STOCKTAKING_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_WAREHOUSE_STAFF)
                        .pathMatchers(HttpMethod.POST, ROLE_URL).hasAnyRole(ROLE_ADMIN)
                        .pathMatchers(HttpMethod.POST, PERMISSION_URL).hasAnyRole(ROLE_ADMIN)
                        .pathMatchers(HttpMethod.POST, USER_URL).hasAnyRole(ROLE_ADMIN, ROLE_SUPPORT)

                        .pathMatchers(HttpMethod.PUT, BRAND_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.PUT, CATEGORY_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.PUT, PRODUCT_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.PUT, PROVIDER_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.PUT, SALE_URL).hasAnyRole(ROLE_ADMIN, ROLE_SALESPERSON)
                        .pathMatchers(HttpMethod.PUT, STOCKTAKING_URL).hasAnyRole(ROLE_ADMIN, ROLE_WAREHOUSE_STAFF)
                        .pathMatchers(HttpMethod.PUT, ROLE_URL).hasAnyRole(ROLE_ADMIN, ROLE_SUPPORT)
                        .pathMatchers(HttpMethod.PUT, PERMISSION_URL).hasAnyRole(ROLE_ADMIN)
                        .pathMatchers(HttpMethod.PUT, USER_URL).hasAnyRole(ROLE_ADMIN, ROLE_SUPPORT)

                        .pathMatchers(HttpMethod.DELETE, BRAND_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.DELETE, CATEGORY_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.DELETE, PRODUCT_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.DELETE, PROVIDER_URL).hasAnyRole(ROLE_ADMIN, ROLE_INVENTORY_MANAGER)
                        .pathMatchers(HttpMethod.DELETE, SALE_URL).hasAnyRole(ROLE_ADMIN, ROLE_SALESPERSON)
                        .pathMatchers(HttpMethod.DELETE, STOCKTAKING_URL).hasAnyRole(ROLE_ADMIN, ROLE_WAREHOUSE_STAFF)
                        .pathMatchers(HttpMethod.DELETE, ROLE_URL).hasAnyRole(ROLE_ADMIN)
                        .pathMatchers(HttpMethod.DELETE, PERMISSION_URL).hasAnyRole(ROLE_ADMIN)
                        .pathMatchers(HttpMethod.DELETE, USER_URL).hasAnyRole(ROLE_ADMIN)

                        .anyExchange().authenticated()
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterAt(authTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
