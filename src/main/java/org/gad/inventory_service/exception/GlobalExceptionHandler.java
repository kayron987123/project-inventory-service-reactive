package org.gad.inventory_service.exception;

import jakarta.validation.ConstraintViolationException;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.gad.inventory_service.utils.Constants.MESSAGE_VALIDATION_INCORRECT;

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

        return buildErrorResponse(exchange, HttpStatus.BAD_REQUEST, MESSAGE_VALIDATION_INCORRECT, errors);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleWebExchangeBindException(WebExchangeBindException ex, ServerWebExchange exchange) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildErrorResponse(exchange, HttpStatus.BAD_REQUEST, MESSAGE_VALIDATION_INCORRECT, errors);
    }

    @ExceptionHandler({
            ProductNotFoundException.class,
            ProviderNotFoundException.class,
            CategoryNotFoundException.class,
            BrandNotFoundException.class,
            StockTakingNotFoundException.class,
            SalesNotFoundException.class,
            UserNotFoundException.class,
            RoleNotFoundException.class,
            PermissionNotFoundException.class
    })
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundExceptions(RuntimeException ex, ServerWebExchange exchange) {
        return buildErrorResponse(exchange, HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(ProviderAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleProviderAlreadyExistsException(ProviderAlreadyExistsException ex, ServerWebExchange exchange) {
        return buildErrorResponse(exchange, HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(ExcelReportGenerationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleExcelReportGenerationException(ExcelReportGenerationException ex, ServerWebExchange exchange) {
        return buildErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    @ExceptionHandler({
            InvalidDateRangeException.class,
            InvalidDateFormatException.class,
            InvalidPriceRangeException.class
    })
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidDateRangeException(RuntimeException ex, ServerWebExchange exchange) {
        return buildErrorResponse(exchange, HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBadCredentialsException(BadCredentialsException ex, ServerWebExchange exchange) {
        return buildErrorResponse(exchange, HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    private Mono<ResponseEntity<ErrorResponse>> buildErrorResponse(ServerWebExchange exchange,
                                                                   HttpStatus status,
                                                                   String message,
                                                                   List<String> errors) {

        String fullPath = exchange.getRequest().getURI().getPath();
        String query = exchange.getRequest().getURI().getQuery();
        String pathWithParams = query != null ? fullPath + "?" + query : fullPath;

        String methodHttp = exchange.getRequest().getMethod().name();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .errors(errors)
                .timestamp(UtilsMethods.datetimeNowFormatted())
                .path(methodHttp + ": " + pathWithParams)
                .build();

        return Mono.just(ResponseEntity.status(status).body(errorResponse));
    }
}
