package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.ProductDTO;
import org.gad.inventory_service.dto.request.CreateProductRequest;
import org.gad.inventory_service.dto.request.UpdateProductRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.exception.BrandNotFoundException;
import org.gad.inventory_service.exception.CategoryNotFoundException;
import org.gad.inventory_service.exception.ProductNotFoundException;
import org.gad.inventory_service.exception.ProviderNotFoundException;
import org.gad.inventory_service.service.ProductService;
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

import java.math.BigDecimal;

import static org.gad.inventory_service.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
@Import(TestConfig.class)
@WithMockUser
class ProductControllerTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private WebTestClient webTestClient;

    private final String idProduct = "665c2e2f8b3e2a6b7c8d9e0f";
    private Flux<ProductDTO> products;
    private Mono<ProductDTO> product;
    private CreateProductRequest createProductRequest;
    private UpdateProductRequest updateProductRequest;

    @BeforeEach
    void setUp() {
        products = Flux.just(
                ProductDTO.builder()
                        .name("Product A")
                        .price(BigDecimal.valueOf(100.00))
                        .build(),
                ProductDTO.builder()
                        .name("Product B")
                        .price(BigDecimal.valueOf(200.00))
                        .build());

        product = Mono.just(
                ProductDTO.builder()
                        .idProduct(idProduct)
                        .name("Product A")
                        .price(BigDecimal.valueOf(100.00))
                        .build());

        createProductRequest = CreateProductRequest.builder()
                .name("New Product")
                .description("Description of new product")
                .price(BigDecimal.valueOf(150.00))
                .categoryName("Category A")
                .brandName("Brand A")
                .providerName("Provider A")
                .build();

        updateProductRequest = UpdateProductRequest.builder()
                .name("Updated Product")
                .description("Updated description")
                .price(BigDecimal.valueOf(120.00))
                .categoryName("Category B")
                .brandName("Brand B")
                .providerName("Provider B")
                .build();
        Mockito.reset(productService);
    }

    @Test
    void getAllProducts_ShouldReturnAllProductsAndStatus200_WhenProductsExist() {
        when(productService.findAllProducts()).thenReturn(products);

        webTestClient.get()
                .uri("/api/v1/products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Products retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllProducts_ShouldThrowAndReturnStatus404_WhenProductsDoNotExist() {
        when(productService.findAllProducts()).thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_FLUX));

        webTestClient.get()
                .uri("/api/v1/products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("No products found", dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllProductsBySearch_ShouldReturnProductsAndStatus200_WhenSearchCriteriaMatch() {
        String name = "Product A";
        String categoryName = "Category A";
        String brandName = "Brand A";
        String providerName = "Provider A";

        when(productService.findProductsByCriteria(anyString(), anyString(), anyString(), anyString())).thenReturn(products);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/products/search")
                        .queryParam("name", name)
                        .queryParam("categoryName", categoryName)
                        .queryParam("brandName", brandName)
                        .queryParam("providerName", providerName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Products retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllProductsBySearch_ShouldThrowAndReturnStatus404_WhenProductsDoNotMatchSearchCriteria() {
        String name = "Nonexistent Product";
        String categoryName = "Nonexistent Category";
        String brandName = "Nonexistent Brand";
        String providerName = "Nonexistent Provider";

        when(productService.findProductsByCriteria(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_FLUX_CRITERIA));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/products/search")
                        .queryParam("name", name)
                        .queryParam("categoryName", categoryName)
                        .queryParam("brandName", brandName)
                        .queryParam("providerName", providerName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("No products found with the given criteria", dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllProductsBySearch_ShouldThrowAndReturnStatus404_WhenProductWithCategoryNameDoesNotExist() {
        String name = "Product A";
        String categoryName = "Nonexistent Category";
        String brandName = "Brand A";
        String providerName = "Provider A";

        when(productService.findProductsByCriteria(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new CategoryNotFoundException(CATEGORY_NOT_FOUND_NAME + categoryName));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/products/search")
                        .queryParam("name", name)
                        .queryParam("categoryName", categoryName)
                        .queryParam("brandName", brandName)
                        .queryParam("providerName", providerName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Category not found with name: " + categoryName, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllProductsBySearch_ShouldThrowAndReturnStatus404_WhenProductWithBrandNameDoesNotExist() {
        String name = "Product A";
        String categoryName = "Category A";
        String brandName = "Nonexistent Brand";
        String providerName = "Provider A";

        when(productService.findProductsByCriteria(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new BrandNotFoundException(BRAND_NOT_FOUND_NAME + brandName));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/products/search")
                        .queryParam("name", name)
                        .queryParam("categoryName", categoryName)
                        .queryParam("brandName", brandName)
                        .queryParam("providerName", providerName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Brand not found with name: " + brandName, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllProductsBySearch_ShouldThrowAndReturnStatus404_WhenProductWithProviderNameDoesNotExist() {
        String name = "Product A";
        String categoryName = "Category A";
        String brandName = "Brand A";
        String providerName = "Nonexistent Provider";

        when(productService.findProductsByCriteria(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new ProviderNotFoundException(PROVIDER_NOT_FOUND_NAME + providerName));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/products/search")
                        .queryParam("name", name)
                        .queryParam("categoryName", categoryName)
                        .queryParam("brandName", brandName)
                        .queryParam("providerName", providerName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Provider not found with name : " + providerName, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getProductById_ShouldReturnProductAndStatus200_WhenProductExists() {
        when(productService.findProductById(anyString())).thenReturn(product);

        webTestClient.get()
                .uri("/api/v1/products/{id}", idProduct)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Product retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getProductById_ShouldThrowAndReturnStatus404_WhenProductDoesNotExist() {
        when(productService.findProductById(anyString())).thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idProduct));

        webTestClient.get()
                .uri("/api/v1/products/{id}", idProduct)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Product not found with id: " + idProduct, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void createProduct_ShouldReturnCreatedProductAndStatus201_WhenProductIsCreated() {
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(product);

        webTestClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createProductRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Product created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProductAndStatus201_WhenProductIsUpdated() {
        when(productService.updateProduct(anyString(), any(UpdateProductRequest.class))).thenReturn(product);

        webTestClient.put()
                .uri("/api/v1/products/{id}", idProduct)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateProductRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Product updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateProduct_ShouldThrowAndReturnStatus404_WhenProductDoesNotExist() {
        when(productService.updateProduct(anyString(), any(UpdateProductRequest.class)))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idProduct));

        webTestClient.put()
                .uri("/api/v1/products/{id}", idProduct)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateProductRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Product not found with id: " + idProduct, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void deleteProductById_ShouldReturnNoContentAndStatus204_WhenProductIsDeleted() {
        when(productService.deleteProductById(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/products/{id}", idProduct)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));
    }

    @Test
    void deleteProductById_ShouldThrowAndReturnStatus404_WhenProductDoesNotExist() {
        when(productService.deleteProductById(anyString()))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idProduct));

        webTestClient.delete()
                .uri("/api/v1/products/{id}", idProduct)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Product not found with id: " + idProduct, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }
}