package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Brand;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BrandRepository extends ReactiveMongoRepository<Brand, UUID> {
    Mono<Brand> findBrandByNameContainingIgnoreCase(String name);
}
