package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.repository.custom.ProductRepositoryCustom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface ProductRepository extends ReactiveMongoRepository<Product, String>, ProductRepositoryCustom {
    Mono<Product> findProductByNameContainingIgnoreCase(String name);
}
