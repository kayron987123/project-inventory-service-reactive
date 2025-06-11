package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.CategoryDTO;
import org.gad.inventory_service.dto.request.CreateCategoryRequest;
import org.gad.inventory_service.dto.request.UpdateCategoryRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.gad.inventory_service.exception.CategoryNotFoundException;
import org.gad.inventory_service.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(CategoryController.class)
@Import(TestConfig.class)
@WithMockUser
class CategoryControllerTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WebTestClient webTestClient;

    private Flux<CategoryDTO> categories;
    private Mono<CategoryDTO> category;
    private final String idCategory = "665c2e2f8b3e2a6b7c8d9e0f";
    private CreateCategoryRequest createCategoryRequest;
    private UpdateCategoryRequest updateCategoryRequest;

    @BeforeEach
    void setUp() {
        categories = Flux.just(
                CategoryDTO.builder()
                        .name("Electronics")
                        .build(),
                CategoryDTO.builder()
                        .name("Books")
                        .build());

        category = Mono.just(
                CategoryDTO.builder()
                        .name("Electronics")
                        .build());

        createCategoryRequest = CreateCategoryRequest.builder()
                .name("Electronics")
                .build();

        updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("Updated Electronics")
                .build();
        Mockito.reset(categoryService);
    }

    @Test
    void findAllCategories_ShouldReturnAllCategoriesAndStatus200_WhenCategoriesExist() {
        when(categoryService.findAllCategories()).thenReturn(categories);

        webTestClient.get()
                .uri("/api/v1/categories")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Categories retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findAllCategories_ShouldThrowAndReturnStatus404_WhenNoCategoriesExist() {
        when(categoryService.findAllCategories()).thenThrow(new CategoryNotFoundException(CATEGORY_NOT_FOUND_FLUX));

        webTestClient.get()
                .uri("/api/v1/categories")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No categories found", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findCategoryByName_ShouldReturnCategoryAndStatus200_WhenCategoryExists() {
        String categoryName = "Elect";
        when(categoryService.findCategoryByName(anyString())).thenReturn(category);

        webTestClient.get()
                .uri("/api/v1/categories/search?name=" + categoryName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Category retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findCategoryByName_ShouldThrowAndReturnStatus404_WhenCategoryDoesNotExist() {
        String categoryName = "NonExistentCategory";
        when(categoryService.findCategoryByName(anyString()))
                .thenThrow(new CategoryNotFoundException(CATEGORY_NOT_FOUND_NAME + categoryName));

        webTestClient.get()
                .uri("/api/v1/categories/search?name=" + categoryName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Category not found with name: " + categoryName, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findCategoryById_ShouldReturnCategoryAndStatus200_WhenCategoryExists() {
        when(categoryService.findCategoryById(anyString())).thenReturn(category);

        webTestClient.get()
                .uri("/api/v1/categories/" + idCategory)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Category retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findCategoryById_ShouldThrowAndReturnStatus404_WhenCategoryDoesNotExist() {
        when(categoryService.findCategoryById(anyString()))
                .thenThrow(new CategoryNotFoundException(CATEGORY_NOT_FOUND_ID + idCategory));

        webTestClient.get()
                .uri("/api/v1/categories/" + idCategory)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Category not found with id: " + idCategory, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void createCategory_ShouldReturnCreatedCategoryAndStatus201_WhenCategoryIsValid() {
        when(categoryService.saveCategory(any(CreateCategoryRequest.class))).thenReturn(category);

        webTestClient.post()
                .uri("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createCategoryRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Category created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategoryAndStatus201_WhenCategoryExists() {
        when(categoryService.updateCategory(anyString(), any(UpdateCategoryRequest.class)))
                .thenReturn(category);

        webTestClient.put()
                .uri("/api/v1/categories/" + idCategory)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateCategoryRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Category updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateCategory_ShouldThrowAndReturnStatus404_WhenCategoryDoesNotExist() {
        when(categoryService.updateCategory(anyString(), any(UpdateCategoryRequest.class)))
                .thenThrow(new CategoryNotFoundException(CATEGORY_NOT_FOUND_ID + idCategory));

        webTestClient.put()
                .uri("/api/v1/categories/" + idCategory)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateCategoryRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Category not found with id: " + idCategory, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void deleteCategoryById_ShouldReturnNoContentAndStatus204_WhenCategoryExists() {
        when(categoryService.deleteCategoryById(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/categories/" + idCategory)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));
    }

    @Test
    void deleteCategoryById_ShouldThrowAndReturnStatus404_WhenCategoryDoesNotExist() {
        when(categoryService.deleteCategoryById(anyString()))
                .thenThrow(new CategoryNotFoundException(CATEGORY_NOT_FOUND_ID + idCategory));

        webTestClient.delete()
                .uri("/api/v1/categories/" + idCategory)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Category not found with id: " + idCategory, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }
}