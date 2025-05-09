package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.repository.custom.ProductRepositoryCustom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface ProductRepository extends ReactiveMongoRepository<Product, UUID>, ProductRepositoryCustom {
}
