package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    Mono<Category> findCategoryByNameContainingIgnoreCase(String name);
}
