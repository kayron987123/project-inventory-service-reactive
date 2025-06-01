package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.request.CreateProductRequest;
import org.gad.inventory_service.dto.ProductDTO;
import org.gad.inventory_service.dto.request.UpdateProductRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductService {
    Flux<ProductDTO> findAllProducts();
    Mono<ProductDTO> findProductById(String id);
    Flux<ProductDTO> findProductsByCriteria(String name,
                                  String categoryName,
                                  String brandName,
                                  String providerName);
    Mono<ProductDTO> createProduct(CreateProductRequest createProductRequest);
    Mono<ProductDTO> updateProduct(String id, UpdateProductRequest updateProductRequest);
    Mono<Void> deleteProductById(String id);
}
