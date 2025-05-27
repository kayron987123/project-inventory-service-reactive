package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.CategoryDTO;
import org.gad.inventory_service.dto.request.CreateCategoryRequest;
import org.gad.inventory_service.dto.request.UpdateCategoryRequest;
import org.gad.inventory_service.exception.CategoryNotFoundException;
import org.gad.inventory_service.model.Category;
import org.gad.inventory_service.repository.CategoryRepository;
import org.gad.inventory_service.service.CategoryService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Mono<CategoryDTO> findCategoryById(String id) {
        return findCategory(categoryRepository.findById(id),
                CATEGORY_NOT_FOUND_ID + id);
    }

    @Override
    public Mono<CategoryDTO> findCategoryByName(String name) {
        return findCategory(categoryRepository.findCategoryByNameContainingIgnoreCase(name),
                CATEGORY_NOT_FOUND_NAME + name);
    }

    @Override
    public Flux<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll()
                .switchIfEmpty(Flux.error(new CategoryNotFoundException(CATEGORY_NOT_FOUND_FLUX)))
                .map(Mappers::categoryToDTO)
                .doOnError(error -> log.error(CATEGORY_SEARCHING_BRANDS, error.getMessage()));
    }

    @Override
    public Mono<CategoryDTO> saveCategory(CreateCategoryRequest createCategoryRequest) {
        Category category = Category.builder()
                .name(createCategoryRequest.name())
                .build();

        return categoryRepository.save(category)
                .map(Mappers::categoryToDTO)
                .doOnError(error -> log.error(CATEGORY_SAVING_BRAND, error.getMessage()));
    }

    @Override
    public Mono<CategoryDTO> updateCategory(String id, UpdateCategoryRequest updateCategoryRequest) {
        Mono<Category> categoryMono = findById(id);

        return categoryMono
                .flatMap(category -> {
                    category.setName(updateCategoryRequest.name());
                    return categoryRepository.save(category);
                })
                .map(Mappers::categoryToDTO)
                .doOnError(error -> log.error(CATEGORY_UPDATING_BRAND, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteCategoryById(String id) {
        Mono<Category> categoryMono = findById(id);

        return categoryMono
                .flatMap(category -> categoryRepository.deleteById(category.getIdCategory()))
                .doOnError(error -> log.error(CATEGORY_DELETING_BRAND, error.getMessage()));
    }

    private Mono<Category> findById(String id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException(CATEGORY_NOT_FOUND_ID + id)));
    }

    private Mono<CategoryDTO> findCategory(Mono<Category> categoryMono, String errorMessage) {
        return categoryMono
                .switchIfEmpty(Mono.error(new CategoryNotFoundException(errorMessage)))
                .map(Mappers::categoryToDTO)
                .doOnError(error -> log.error(Constants.CATEGORY_SEARCHING_BRAND, error.getMessage()));
    }
}
