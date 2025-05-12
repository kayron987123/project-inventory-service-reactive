package org.gad.inventory_service.exception;

import jakarta.validation.ConstraintViolationException;
import org.gad.inventory_service.dto.ErrorResponse;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException ex, ServerWebExchange exchange) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String[] pathParts = violation.getPropertyPath().toString().split("\\.");
                    return pathParts[pathParts.length - 1] + ": " + violation.getMessage();
                })
                .sorted()
                .toList();

        return buildErrorResponse(exchange, HttpStatus.BAD_REQUEST, "Validation incorrect", errors);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleWebExchangeBindException(WebExchangeBindException ex, ServerWebExchange exchange) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildErrorResponse(exchange, HttpStatus.BAD_REQUEST, "Validation incorrect", errors);
    }

    @ExceptionHandler({
            ProductNotFoundException.class,
            ProviderNotFoundException.class,
            CategoryNotFoundException.class,
            BrandNotFoundException.class
    })
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundExceptions(RuntimeException ex, ServerWebExchange exchange) {
        return buildErrorResponse(exchange, HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    private Mono<ResponseEntity<ErrorResponse>> buildErrorResponse(ServerWebExchange exchange,
                                                                   HttpStatus status,
                                                                   String message,
                                                                   List<String> errors) {

        String methodHttp = exchange.getRequest().getMethod().name();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .errors(errors)
                .timestamp(UtilsMethods.datetimeNowFormatted())
                .path(methodHttp + ": " + exchange.getRequest().getPath())
                .build();

        return Mono.just(ResponseEntity.status(status).body(errorResponse));
    }
}
