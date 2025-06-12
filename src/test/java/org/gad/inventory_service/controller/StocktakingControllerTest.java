package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.StocktakingDTO;
import org.gad.inventory_service.dto.request.CreateStocktakingRequest;
import org.gad.inventory_service.dto.request.UpdateStocktakingRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.gad.inventory_service.exception.ProductNotFoundException;
import org.gad.inventory_service.exception.StockTakingNotFoundException;
import org.gad.inventory_service.service.StocktakingService;
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

@WebFluxTest(StocktakingController.class)
@Import(TestConfig.class)
@WithMockUser
class StocktakingControllerTest {
    @Autowired
    private StocktakingService stocktakingService;

    @Autowired
    private WebTestClient webTestClient;

    private final String idStocktaking = "665c2e2f8b3e2a6b7c8d9e0f";
    private Flux<StocktakingDTO> stocktakings;
    private Mono<StocktakingDTO> stocktaking;
    private CreateStocktakingRequest createStocktakingRequest;
    private UpdateStocktakingRequest updateStocktakingRequest;

    @BeforeEach
    void setUp() {
        stocktakings = Flux.just(
                StocktakingDTO.builder()
                        .productName("Product A")
                        .build(),
                StocktakingDTO.builder()
                        .productName("Product B")
                        .build()
        );
        stocktaking = Mono.just(
                StocktakingDTO.builder()
                        .productName("Product A")
                        .build()
        );
        createStocktakingRequest = CreateStocktakingRequest.builder()
                .productName("Product A")
                .quantity(100)
                .build();
        updateStocktakingRequest = UpdateStocktakingRequest.builder()
                .productName("Product A")
                .quantity(150)
                .build();
        Mockito.reset(stocktakingService);
    }

    @Test
    void getAllStocktaking_ShouldReturnAllStocktakingsAndStatus200_WhenStocktakingExists() {
        when(stocktakingService.findAllStocktaking()).thenReturn(stocktakings);

        webTestClient.get()
                .uri("/api/v1/stocktaking")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Stocktaking retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllStocktaking_ShouldThrowAndReturnStatus404_WhenNoStocktakingsExist() {
        when(stocktakingService.findAllStocktaking()).thenThrow(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND));

        webTestClient.get()
                .uri("/api/v1/stocktaking")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No stocktaking found", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getAllStocktaking_ShouldThrowAndReturnStatus404_WhenProductOfResultIsNotFound() {
        when(stocktakingService.findAllStocktaking()).thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idStocktaking));

        webTestClient.get()
                .uri("/api/v1/stocktaking")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getStocktakingByDateRange_ShouldReturnStocktakingsWithinDateRangeAndStatus200_WhenStocktakingsExist() {
        String startDate = "2023-01-01 00:00:00";
        String endDate = "2023-12-31 23:59:59";
        when(stocktakingService.findAllStocktakingByDateBetween(anyString(), anyString())).thenReturn(stocktakings);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/stocktaking/date-range")
                        .queryParam("startDate", startDate)
                        .queryParam("endDate", endDate)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Stocktaking retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getStocktakingByDateRange_ShouldThrowAndReturnStatus404_WhenNoStocktakingsExist() {
        String startDate = "2023-01-01 00:00:00";
        String endDate = "2023-12-31 23:59:59";
        when(stocktakingService.findAllStocktakingByDateBetween(anyString(), anyString()))
                .thenThrow(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/stocktaking/date-range")
                        .queryParam("startDate", startDate)
                        .queryParam("endDate", endDate)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No stocktaking found", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getStocktakingByDateRange_ShouldThrowAndReturnStatus404_WhenProductOfResultIsNotFound() {
        String startDate = "2023-01-01 00:00:00";
        String endDate = "2023-12-31 23:59:59";
        when(stocktakingService.findAllStocktakingByDateBetween(anyString(), anyString()))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idStocktaking));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/stocktaking/date-range")
                        .queryParam("startDate", startDate)
                        .queryParam("endDate", endDate)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getStocktakingByProductName_ShouldReturnStocktakingsByProductNameAndStatus200_WhenStocktakingsExist() {
        String productName = "Product A";
        when(stocktakingService.findAllStocktakingByProductName(anyString())).thenReturn(stocktakings);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/stocktaking/product-name")
                        .queryParam("productName", productName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Stocktaking retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getStocktakingByProductName_ShouldThrowAndReturnStatus404_WhenProductOfSearchIsNotFound() {
        String productName = "Product A";
        when(stocktakingService.findAllStocktakingByProductName(anyString()))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idStocktaking));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/stocktaking/product-name")
                        .queryParam("productName", productName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getStocktakingByProductName_ShouldThrowAndReturnStatus404_WhenNoStocktakingsExist() {
        String productName = "Product A";
        when(stocktakingService.findAllStocktakingByProductName(anyString()))
                .thenThrow(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND_NAME + productName));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/stocktaking/product-name")
                        .queryParam("productName", productName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Stocktaking not found with name: " + productName, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }


    @Test
    void getStocktakingById_ShouldReturnStocktakingByIdAndStatus200_WhenStocktakingExists() {
        when(stocktakingService.findStocktakingById(anyString())).thenReturn(stocktaking);

        webTestClient.get()
                .uri("/api/v1/stocktaking/{id}", idStocktaking)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Stocktaking retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getStocktakingById_ShouldThrowAndReturnStatus404_WhenProductOfResultIsNotFound() {
        when(stocktakingService.findStocktakingById(anyString()))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idStocktaking));

        webTestClient.get()
                .uri("/api/v1/stocktaking/{id}", idStocktaking)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getStocktakingById_ShouldThrowAndReturnStatus404_WhenStocktakingDoesNotExist() {
        when(stocktakingService.findStocktakingById(anyString()))
                .thenThrow(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND_ID + idStocktaking));

        webTestClient.get()
                .uri("/api/v1/stocktaking/{id}", idStocktaking)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Stocktaking not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void createStocktaking_ShouldCreateStocktakingAndReturnStatus201_WhenRequestIsValid() {
        when(stocktakingService.createStocktaking(createStocktakingRequest)).thenReturn(stocktaking);

        webTestClient.post()
                .uri("/api/v1/stocktaking")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createStocktakingRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Stocktaking created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void createStocktaking_ShouldThrowAndReturnStatus404_WhenProductOfRequestIsNotFound() {
        when(stocktakingService.createStocktaking(createStocktakingRequest))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idStocktaking));

        webTestClient.post()
                .uri("/api/v1/stocktaking")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createStocktakingRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void updateStocktaking_ShouldReturnUpdatedStocktakingAndStatus201_WhenRequestIsValid() {
        when(stocktakingService.updateStocktaking(anyString(), any(UpdateStocktakingRequest.class))).thenReturn(stocktaking);

        webTestClient.put()
                .uri("/api/v1/stocktaking/{id}", idStocktaking)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateStocktakingRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Stocktaking updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateStocktaking_ShouldThrowAndReturnStatus404_WhenStocktakingDoesNotExist() {
        when(stocktakingService.updateStocktaking(anyString(), any(UpdateStocktakingRequest.class)))
                .thenThrow(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND_ID + idStocktaking));

        webTestClient.put()
                .uri("/api/v1/stocktaking/{id}", idStocktaking)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateStocktakingRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Stocktaking not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void updateStocktaking_ShouldThrowAndReturnStatus404_WhenProductOfRequestIsNotFound() {
        when(stocktakingService.updateStocktaking(anyString(), any(UpdateStocktakingRequest.class)))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idStocktaking));

        webTestClient.put()
                .uri("/api/v1/stocktaking/{id}", idStocktaking)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateStocktakingRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void deleteStocktakingById_ShouldReturnVoidAndStatus204_WhenStocktakingExists() {
        when(stocktakingService.deleteStocktakingById(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/stocktaking/{id}", idStocktaking)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));
    }

    @Test
    void deleteStocktakingById_ShouldThrowAndReturnStatus404_WhenStocktakingDoesNotExist() {
        when(stocktakingService.deleteStocktakingById(anyString()))
                .thenThrow(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND_ID + idStocktaking));

        webTestClient.delete()
                .uri("/api/v1/stocktaking/{id}", idStocktaking)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Stocktaking not found with id: " + idStocktaking, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }
}