package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreateCategoryRequest;
import org.gad.inventory_service.dto.request.UpdateCategoryRequest;
import org.gad.inventory_service.exception.CategoryNotFoundException;
import org.gad.inventory_service.model.Category;
import org.gad.inventory_service.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.gad.inventory_service.utils.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CreateCategoryRequest createCategoryRequest;
    private UpdateCategoryRequest updateCategoryRequest;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .idCategory("123456")
                .name("Electronics")
                .build();
        createCategoryRequest = CreateCategoryRequest
                .builder()
                .name("Electronics")
                .build();
        updateCategoryRequest = UpdateCategoryRequest
                .builder()
                .name("Updated Electronics")
                .build();
    }

    @Test
    void findCategoryById_ShouldReturnCategoryDTO_WhenCategoryExists() {
        when(categoryRepository.findById(anyString()))
                .thenReturn(Mono.just(category));

        StepVerifier.create(categoryService.findCategoryById("123456"))
                .expectNextMatches(dto ->
                        dto.idCategory().equals("123456") &&
                                dto.name().equals("Electronics"))
                .verifyComplete();
        verify(categoryRepository, times(1)).findById(anyString());
    }

    @Test
    void findCategoryById_ShouldThrow_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(categoryService.findCategoryById("non-existent-id"))
                .expectErrorMatches(throwable -> throwable instanceof CategoryNotFoundException &&
                        throwable.getMessage().equals(CATEGORY_NOT_FOUND_ID + "non-existent-id"))
                .verify();
        verify(categoryRepository, times(1)).findById(anyString());
    }

    @Test
    void findCategoryByName_ShouldReturnCategoryDTO_WhenCategoryExists() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));

        StepVerifier.create(categoryService.findCategoryByName("Electronics"))
                .expectNextMatches(dto ->
                        dto.idCategory().equals("123456") &&
                                dto.name().equals("Electronics"))
                .verifyComplete();
        verify(categoryRepository, times(1)).findCategoryByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findCategoryByName_ShouldThrow_WhenCategoryDoesNotExist() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(categoryService.findCategoryByName("NonExistentCategory"))
                .expectErrorMatches(throwable -> throwable instanceof CategoryNotFoundException &&
                        throwable.getMessage().equals(CATEGORY_NOT_FOUND_NAME + "NonExistentCategory"))
                .verify();
        verify(categoryRepository, times(1)).findCategoryByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findAllCategories_ShouldReturnFluxOfCategoryDTO_WhenCategoriesExist() {
        when(categoryRepository.findAll())
                .thenReturn(Flux.just(category));

        StepVerifier.create(categoryService.findAllCategories())
                .expectNextMatches(dto ->
                        dto.idCategory().equals("123456") &&
                                dto.name().equals("Electronics"))
                .verifyComplete();
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findAllCategories_ShouldThrow_WhenNoCategoriesExist() {
        when(categoryRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(categoryService.findAllCategories())
                .expectErrorMatches(throwable -> throwable instanceof CategoryNotFoundException &&
                        throwable.getMessage().equals(CATEGORY_NOT_FOUND_FLUX))
                .verify();
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void saveCategory_ShouldReturnCategoryDTO_WhenCategoryIsSaved() {
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(Mono.just(category));

        StepVerifier.create(categoryService.saveCategory(createCategoryRequest))
                .expectNextMatches(dto ->
                        dto.idCategory().equals("123456") &&
                                dto.name().equals("Electronics"))
                .verifyComplete();
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategoryDTO_WhenCategoryIsUpdated() {
        when(categoryRepository.findById(anyString()))
                .thenReturn(Mono.just(category));
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(Mono.just(category));

        StepVerifier.create(categoryService.updateCategory("123456", updateCategoryRequest))
                .expectNextMatches(dto ->
                        dto.idCategory().equals("123456") &&
                                dto.name().equals("Updated Electronics"))
                .verifyComplete();
        verify(categoryRepository, times(1)).findById(anyString());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_ShouldThrow_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(categoryService.updateCategory("non-existent-id", updateCategoryRequest))
                .expectErrorMatches(throwable -> throwable instanceof CategoryNotFoundException &&
                        throwable.getMessage().equals(CATEGORY_NOT_FOUND_ID + "non-existent-id"))
                .verify();
        verify(categoryRepository, times(1)).findById(anyString());
    }

    @Test
    void deleteCategoryById_ShouldReturnMonoVoid_WhenCategoryExists() {
        when(categoryRepository.findById(anyString()))
                .thenReturn(Mono.just(category));
        when(categoryRepository.deleteById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(categoryService.deleteCategoryById("123456"))
                .verifyComplete();
        verify(categoryRepository, times(1)).findById(anyString());
        verify(categoryRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteCategoryById_ShouldThrow_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(categoryService.deleteCategoryById("non-existent-id"))
                .expectErrorMatches(throwable -> throwable instanceof CategoryNotFoundException &&
                        throwable.getMessage().equals(CATEGORY_NOT_FOUND_ID + "non-existent-id"))
                .verify();
        verify(categoryRepository, times(1)).findById(anyString());
    }
}