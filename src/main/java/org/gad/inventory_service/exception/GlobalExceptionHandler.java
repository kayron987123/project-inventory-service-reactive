package org.gad.inventory_service.exception;

import org.gad.inventory_service.dto.ErrorResponse;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleProductNotFoundException(ProductNotFoundException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null,
                UtilsMethods.datetimeNowFormatted(),
                exchange.getRequest().getPath().toString()
        );
        return Mono.just(ResponseEntity.status(404).body(errorResponse));
    }

    @ExceptionHandler(ProviderNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleProviderNotFoundException(ProviderNotFoundException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null,
                UtilsMethods.datetimeNowFormatted(),
                exchange.getRequest().getPath().toString()
        );
        return Mono.just(ResponseEntity.status(404).body(errorResponse));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleCategoryNotFoundException(CategoryNotFoundException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null,
                UtilsMethods.datetimeNowFormatted(),
                exchange.getRequest().getPath().toString()
        );
        return Mono.just(ResponseEntity.status(404).body(errorResponse));
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBrandNotFoundException(BrandNotFoundException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null,
                UtilsMethods.datetimeNowFormatted(),
                exchange.getRequest().getPath().toString()
        );
        return Mono.just(ResponseEntity.status(404).body(errorResponse));
    }
}
