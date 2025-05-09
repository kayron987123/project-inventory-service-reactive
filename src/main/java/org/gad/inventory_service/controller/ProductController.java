package org.gad.inventory_service.controller;

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


@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> getAllProducts(@RequestParam(required = false) @Pattern(regexp = "^[a-zA-Z]+$", message = "Just letters are accepted") String name,
                                                             @RequestParam(required = false) @Pattern(regexp = "^[a-zA-Z]+$", message = "Just letters are accepted") String category,
                                                             @RequestParam(required = false) @Pattern(regexp = "^[a-zA-Z]+$", message = "Just letters are accepted") String brand,
                                                             @RequestParam(required = false) @Pattern(regexp = "^[a-zA-Z]+$", message = "Just letters are accepted") String provider) {
        return productService.findProducts(name, category, brand, provider)
                .collectList()
                .map(products -> ResponseEntity.ok().body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Products found",
                        products,
                        UtilsMethods.datetimeNowFormatted()
                )));
    }

    @GetMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> getProductByUuid(@PathVariable String uuid) {
        return productService.findProductByUuid(uuid)
                .map(product -> ResponseEntity.ok().body(new DataResponse(
                        HttpStatus.OK.value(),
                        "Product found",
                        product,
                        UtilsMethods.datetimeNowFormatted()
                )));
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        return productService.createProduct(createProductRequest)
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                        HttpStatus.CREATED.value(),
                        "Product created",
                        product,
                        UtilsMethods.datetimeNowFormatted()
                )));
    }

    @PutMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> updateProduct(@PathVariable String uuid, @RequestBody UpdateProductRequest updateProductRequest) {
        return productService.updateProduct(uuid, updateProductRequest)
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                        HttpStatus.CREATED.value(),
                        "Product updated",
                        product,
                        UtilsMethods.datetimeNowFormatted()
                )));
    }

    @DeleteMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> deleteProduct(@PathVariable String uuid) {
        return productService.deleteProduct(uuid)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DataResponse(
                        HttpStatus.NO_CONTENT.value(),
                        "Product deleted",
                        null,
                        UtilsMethods.datetimeNowFormatted()
                ))));
    }
}
