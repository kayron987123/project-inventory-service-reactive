package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.CategoryDTO;
import org.gad.inventory_service.dto.request.CreateCategoryRequest;
import org.gad.inventory_service.dto.request.UpdateCategoryRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    Mono<CategoryDTO> findCategoryById(String id);
    Mono<CategoryDTO> findCategoryByName(String name);
    Flux<CategoryDTO> findAllCategories();
    Mono<CategoryDTO> saveCategory(CreateCategoryRequest createCategoryRequest);
    Mono<CategoryDTO> updateCategory(String id, UpdateCategoryRequest updateCategoryRequest);
    Mono<Void> deleteCategoryById(String id);
}
