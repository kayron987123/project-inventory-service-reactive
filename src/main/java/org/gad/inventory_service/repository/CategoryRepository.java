package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CategoryRepository extends ReactiveMongoRepository<Category, UUID> {
    Mono<Category> findCategoryByNameIgnoreCase(String name);
}
