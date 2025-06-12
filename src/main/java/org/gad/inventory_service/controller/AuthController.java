package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.config.jwt.JwtUtils;
import org.gad.inventory_service.dto.request.LoginRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.gad.inventory_service.utils.Constants.LOGIN_TOKEN_SUCCESS;
import static org.gad.inventory_service.utils.UtilsMethods.datetimeNowFormatted;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final ReactiveAuthenticationManager authenticationManager;

    @PostMapping("/login")
    public Mono<ResponseEntity<DataResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

        return authenticationManager.authenticate(authToken)
                .flatMap(auth -> jwtUtils.generateToken(auth)
                        .map(token -> ResponseEntity.ok(DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(LOGIN_TOKEN_SUCCESS)
                                .data(Map.of("token", token))
                                .timestamp(datetimeNowFormatted())
                                .build())));
    }
}
