package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateProductRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.request.UpdateProductRequest;
import org.gad.inventory_service.service.ProductService;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.gad.inventory_service.utils.Constants.*;

@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> getAllProducts() {
        return productService.findAllProducts()
                .collectList()
                .map(products -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PRODUCTS_OK)
                                .data(products)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }


    @GetMapping("/search")
    public Mono<ResponseEntity<DataResponse>> getAllProducts(@RequestParam @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_NAME)
                                                             @NotBlank(message = MESSAGE_NAME_PRODUCT_CANNOT_BE_EMPTY) String name,
                                                             @RequestParam @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_CATEGORY)
                                                             @NotBlank(message = MESSAGE_NAME_CATEGORY_CANNOT_BE_EMPTY) String categoryName,
                                                             @RequestParam @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_BRAND)
                                                             @NotBlank(message = MESSAGE_NAME_BRAND_CANNOT_BE_EMPTY) String brandName,
                                                             @RequestParam @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_PROVIDER)
                                                             @NotBlank(message = MESSAGE_NAME_PROVIDER_CANNOT_BE_EMPTY) String providerName) {
        return productService.findProductsByCriteria(name, categoryName, brandName, providerName)
                .collectList()
                .map(products -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PRODUCTS_OK)
                                .data(products)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> getProductById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                             @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return productService.findProductById(id)
                .map(product -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PRODUCT_OK)
                                .data(product)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        return productService.createProduct(createProductRequest)
                .map(product -> {
                    URI location = UtilsMethods.createUri(PRODUCT_URI, product.idProduct());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_PRODUCT_CREATED)
                            .data(product)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> updateProduct(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                            @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id,
                                                            @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        return productService.updateProduct(id, updateProductRequest)
                .map(product -> {
                    URI location = UtilsMethods.createUri(PRODUCT_URI, product.idProduct());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_PRODUCT_UPDATED)
                            .data(product)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProductById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                        @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return productService.deleteProductById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
