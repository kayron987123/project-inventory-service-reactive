package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.config.jwt.JwtUtils;
import org.gad.inventory_service.dto.request.LoginRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(AuthController.class)
@Import(TestConfig.class)
class AuthControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    @Qualifier("reactiveAuthenticationManager")
    private ReactiveAuthenticationManager authenticationManager;

    private LoginRequest loginRequest;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .username("testUser")
                .password("testPassword")
                .build();
        authentication = new UsernamePasswordAuthenticationToken("testUser", "testPassword", Collections.emptyList());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(authentication));

        when(jwtUtils.generateToken(any(Authentication.class)))
                .thenReturn(Mono.just("mocked-test-token"));

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    Object dataObj = dataResponse.data();
                    @SuppressWarnings("unchecked")
                    Map<String, String> data = assertInstanceOf(Map.class, dataObj);
                    assert data.get("token").equals("mocked-test-token");
                    assert dataResponse.status() == 200;
                    assert dataResponse.message().equals("Login and Token generated successfully");
                    assert dataResponse.timestamp() != null;
                });
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.error(new BadCredentialsException("Bad credentials")));

        webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assert errorResponse.status() == 401;
                    assert errorResponse.message().equals("Bad credentials");
                    assert errorResponse.errors() == null;
                    assert errorResponse.timestamp() != null;
                    assert errorResponse.path().equals("POST: /api/v1/auth/login");
                });
    }
}