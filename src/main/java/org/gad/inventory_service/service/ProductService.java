package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.CreateProductRequest;
import org.gad.inventory_service.dto.ProductDTO;
import org.gad.inventory_service.dto.UpdateProductRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

public interface ProductService {
    Mono<ProductDTO> findProductByUuid(String uuid);
    Flux<ProductDTO> findProductsPaginated(Pageable pageable);
    Mono<ProductDTO> createProduct(CreateProductRequest createProductRequest);
    Mono<ProductDTO> updateProduct(String uuid, UpdateProductRequest updateProductRequest);
    Mono<Void> deleteProduct(String uuid);
}
