package org.gad.inventory_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.BrandDTO;
import org.gad.inventory_service.dto.request.CreateBrandRequest;
import org.gad.inventory_service.dto.request.UpdateBrandRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.gad.inventory_service.exception.BrandNotFoundException;
import org.gad.inventory_service.service.BrandService;
import org.gad.inventory_service.utils.Constants;
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


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(BrandController.class)
@Import(TestConfig.class)
@WithMockUser
class BrandControllerTest {
    @Autowired
    private BrandService brandService;

    @Autowired
    private WebTestClient webTestClient;

    private Flux<BrandDTO> brands;
    private Mono<BrandDTO> brand;
    private final String idBrand = "665c2e2f8b3e2a6b7c8d9e0f";
    private CreateBrandRequest createBrandRequest;
    private UpdateBrandRequest updateBrandRequest;

    @BeforeEach
    void setUp() {
        brand = Mono.just(
                BrandDTO.builder()
                        .idBrand("100")
                        .name("Brand Z")
                        .build());

        brands = Flux.just(
                BrandDTO.builder()
                        .idBrand("1")
                        .name("Brand A")
                        .build(),
                BrandDTO.builder()
                        .idBrand("2")
                        .name("Brand B")
                        .build());

        createBrandRequest = CreateBrandRequest.builder()
                .brandName("New Brand")
                .build();

        updateBrandRequest = UpdateBrandRequest.builder()
                .brandName("Updated Brand")
                .build();

        Mockito.reset(brandService);
    }

    @Test
    void findAllBrands_ShouldReturnAllBrandsAndStatus200_WhenBrandsExist() {
        when(brandService.findAllBrands())
                .thenReturn(brands);

        webTestClient.get()
                .uri("/api/v1/brands")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Brands retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findAllBrands_ShouldThrowAndReturnStatus404_WhenBrandsIsEmpty() {
        when(brandService.findAllBrands())
                .thenThrow(new BrandNotFoundException(Constants.BRAND_NOT_FOUND_FLUX));

        webTestClient.get()
                .uri("/api/v1/brands")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No brands found", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertEquals("GET: /api/v1/brands", errorResponse.path());
                });
    }

    @Test
    void findBrandByName_ShouldReturnBrandAndStatus200_WhenBrandExists() {
        when(brandService.findBrandByName(anyString()))
                .thenReturn(brand);

        webTestClient.get()
                .uri("/api/v1/brands/search?name=" + "Brand Z")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Brand retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findBrandByName_ShouldThrowAndReturnStatus404_WhenBrandDoesNotExist() {
        when(brandService.findBrandByName(anyString()))
                .thenThrow(new BrandNotFoundException(Constants.BRAND_NOT_FOUND_NAME + "Brand X"));

        webTestClient.get()
                .uri("/api/v1/brands/search?name=" + "Brand X")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Brand not found with name: Brand X", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertEquals("GET: /api/v1/brands/search?name=Brand X", errorResponse.path());
                });
    }

    @Test
    void findBrandById_ShouldReturnBrandAndStatus200_WhenBrandExists() {
        when(brandService.findBrandById(anyString()))
                .thenReturn(brand);

        webTestClient.get()
                .uri("/api/v1/brands/" + idBrand)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Brand retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findBrandById_ShouldThrowAndReturnStatus404_WhenBrandDoesNotExist() {
        when(brandService.findBrandById(anyString()))
                .thenThrow(new BrandNotFoundException(Constants.BRAND_NOT_FOUND_ID + idBrand));

        webTestClient.get()
                .uri("/api/v1/brands/" + idBrand)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Brand not found with id: " + idBrand, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertEquals("GET: /api/v1/brands/" + idBrand, errorResponse.path());
                });
    }

    @Test
    void createBrand_ShouldReturnCreatedBrandAndStatus201_WhenBrandIsCreated() throws JsonProcessingException {
        when(brandService.saveBrand(Mockito.any(CreateBrandRequest.class)))
                .thenReturn(brand);

        webTestClient.post()
                .uri("/api/v1/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createBrandRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Brand created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateBrand_ShouldReturnUpdatedBrandAndStatus201_WhenBrandIsUpdated() throws JsonProcessingException {
        when(brandService.updateBrand(Mockito.anyString(), Mockito.any(UpdateBrandRequest.class)))
                .thenReturn(brand);

        webTestClient.put()
                .uri("/api/v1/brands/" + idBrand)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateBrandRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Brand updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateBrand_ShouldThrowAndReturnStatus404_WhenBrandDoesNotExist() throws JsonProcessingException {
        when(brandService.updateBrand(anyString(), Mockito.any(UpdateBrandRequest.class)))
                .thenThrow(new BrandNotFoundException(Constants.BRAND_NOT_FOUND_ID + idBrand));

        webTestClient.put()
                .uri("/api/v1/brands/" + idBrand)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateBrandRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Brand not found with id: " + idBrand, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertEquals("PUT: /api/v1/brands/" + idBrand, errorResponse.path());
                });
    }

    @Test
    void deleteBrandById_ShouldReturnVoidAndStatus204_WhenBrandIsDeleted() {
        when(brandService.deleteBrandById(anyString()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/brands/" + idBrand)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));
    }

    @Test
    void deleteBrandById_ShouldThrowAndReturnStatus404_WhenBrandDoesNotExist() {
        when(brandService.deleteBrandById(anyString()))
                .thenThrow(new BrandNotFoundException(Constants.BRAND_NOT_FOUND_ID + idBrand));

        webTestClient.delete()
                .uri("/api/v1/brands/" + idBrand)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Brand not found with id: " + idBrand, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertEquals("DELETE: /api/v1/brands/" + idBrand, errorResponse.path());
                });
    }
}