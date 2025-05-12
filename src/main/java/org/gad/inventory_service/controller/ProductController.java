package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.CreateProductRequest;
import org.gad.inventory_service.dto.DataResponse;
import org.gad.inventory_service.dto.UpdateProductRequest;
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
    public Mono<ResponseEntity<DataResponse>> getAllProducts(@RequestParam(required = false) @Pattern(regexp = REGEX_PRODUCTS, message = MESSAGE_PARAMETER_PRODUCT_NAME) String name,
                                                             @RequestParam(required = false) @Pattern(regexp = REGEX_PRODUCTS, message = MESSAGE_PARAMETER_CATEGORY) String category,
                                                             @RequestParam(required = false) @Pattern(regexp = REGEX_PRODUCTS, message = MESSAGE_PARAMETER_BRAND) String brand,
                                                             @RequestParam(required = false) @Pattern(regexp = REGEX_PRODUCTS, message = MESSAGE_PARAMETER_PROVIDER) String provider) {
        return productService.findProductsByCriteria(name, category, brand, provider)
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

    @GetMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> getProductByUuid(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid) {
        return productService.findProductByUuid(uuid)
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

    @PutMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> updateProduct(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid,
                                                            @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        return productService.updateProduct(uuid, updateProductRequest)
                .map(product -> {
                    URI location = UtilsMethods.createUri(PRODUCT_URI, product.idProduct());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message(MESSAGE_PRODUCT_UPDATED)
                            .data(product)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{uuid}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid) {
        return productService.deleteProduct(uuid)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
