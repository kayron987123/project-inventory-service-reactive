package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.SaleDTO;
import org.gad.inventory_service.dto.request.CreateSaleRequest;
import org.gad.inventory_service.dto.request.UpdateSaleRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.gad.inventory_service.exception.ExcelReportGenerationException;
import org.gad.inventory_service.exception.ProductNotFoundException;
import org.gad.inventory_service.exception.SalesNotFoundException;
import org.gad.inventory_service.service.ExcelReportService;
import org.gad.inventory_service.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.gad.inventory_service.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(SaleController.class)
@Import(TestConfig.class)
@WithMockUser
class SaleControllerTest {
    @Autowired
    private SaleService saleService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ExcelReportService excelReportService;

    private final String idSale = "665c2e2f8b3e2a6b7c8d9e0f";
    private Flux<SaleDTO> sales;
    private Mono<SaleDTO> sale;
    private CreateSaleRequest createSaleRequest;
    private UpdateSaleRequest updateSaleRequest;

    @BeforeEach
    void setUp() {
        sales = Flux.just(
                SaleDTO.builder()
                        .nameProduct("Product A")
                        .build(),
                SaleDTO.builder()
                        .nameProduct("Product B")
                        .build()
        );
        sale = Mono.just(SaleDTO.builder()
                .nameProduct("Product X")
                .build());
        createSaleRequest = CreateSaleRequest.builder()
                .nameProduct("Product Z")
                .quantity(10)
                .build();
        updateSaleRequest = UpdateSaleRequest.builder()
                .nameProduct("Updated Product")
                .quantity(5)
                .build();
        Mockito.reset(saleService);
    }

    @Test
    void getAllSales_ShouldReturnAllSalesAndStatus200_WhenSalesExist() {
        when(saleService.getAllSales()).thenReturn(sales);

        webTestClient.get()
                .uri("/api/v1/sales")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Sales retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllSales_ShouldThrowAndReturnStatus404_WhenNoSalesExist() {
        when(saleService.getAllSales()).thenThrow(new SalesNotFoundException(SALE_NOT_FOUND_FLUX));

        webTestClient.get()
                .uri("/api/v1/sales")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No sales found", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getAllSales_ShouldThrowAndReturnStatus404_WhenProductOfResultNotExists() {
        when(saleService.getAllSales()).thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idSale));

        webTestClient.get()
                .uri("/api/v1/sales")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getSalesByNameProduct_ShouldReturnSalesAndStatus200_WhenProductNameExists() {
        String productName = "product";
        when(saleService.getSaleByNameProduct(anyString())).thenReturn(sales);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/name-product")
                        .queryParam("nameProduct", productName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Sales retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getSalesByNameProduct_ShouldThrowAndReturnStatus404_WhenProductNameDoesNotExist() {
        String productName = "nonexistent";
        when(saleService.getSaleByNameProduct(anyString())).thenThrow(new SalesNotFoundException(PRODUCT_NOT_FOUND_NAME + productName));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/name-product")
                        .queryParam("nameProduct", productName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with name : " + productName, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getSalesByNameProduct_ShouldThrowAndReturnStatus404_WhenSaleOfProductNotExists() {
        String productName = "product";
        when(saleService.getSaleByNameProduct(anyString())).thenThrow(new SalesNotFoundException(SALE_NOT_FOUND_NAME + productName));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/name-product")
                        .queryParam("nameProduct", productName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Sale not found with name product: " + productName, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getSalesByNameProduct_ShouldThrowAndReturnStatus404_WhenProductOfResultNotExists() {
        String productName = "product";
        when(saleService.getSaleByNameProduct(anyString())).thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idSale));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/name-product")
                        .queryParam("nameProduct", productName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getSalesByPriceRange_ShouldReturnSalesAndStatus200_WhenPriceRangeExists() {
        BigDecimal minPrice = BigDecimal.valueOf(10);
        BigDecimal maxPrice = BigDecimal.valueOf(100);
        when(saleService.getSaleByTotalPriceRange(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(sales);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/price-range")
                        .queryParam("minPrice", minPrice)
                        .queryParam("maxPrice", maxPrice)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Sales retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getSalesByPriceRange_ShouldThrowAndReturnStatus404_WhenPriceRangeDoesNotExist() {
        BigDecimal minPrice = BigDecimal.valueOf(1000);
        BigDecimal maxPrice = BigDecimal.valueOf(2000);
        when(saleService.getSaleByTotalPriceRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenThrow(new SalesNotFoundException(SALE_NOT_FOUND_BETWEEN_PRICES + minPrice + " and " + maxPrice));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/price-range")
                        .queryParam("minPrice", minPrice)
                        .queryParam("maxPrice", maxPrice)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No sales found between prices: " + minPrice + " and " + maxPrice, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getSalesByPriceRange_ShouldThrowAndReturnStatus404_WhenProductOfResultNotExists() {
        BigDecimal minPrice = BigDecimal.valueOf(10);
        BigDecimal maxPrice = BigDecimal.valueOf(100);
        when(saleService.getSaleByTotalPriceRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idSale));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/price-range")
                        .queryParam("minPrice", minPrice)
                        .queryParam("maxPrice", maxPrice)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getSalesByDateRange_ShouldReturnSalesAndStatus200_WhenDateRangeExists() {
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        when(saleService.getSaleByDateRange(anyString(), anyString())).thenReturn(sales);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/date-range")
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
                    assertEquals("Sales retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getSalesByDateRange_ShouldThrowAndReturnStatus404_WhenNoSalesExistInDateRange() {
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        when(saleService.getSaleByDateRange(anyString(), anyString()))
                .thenThrow(new SalesNotFoundException(SALE_NOT_FOUND_BETWEEN_DATES + startDate + " and " + endDate));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/date-range")
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
                    assertEquals("No sales found between dates: " + startDate + " and " + endDate, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getSalesByDateRange_ShouldThrowAndReturnStatus404_WhenProductOfResultNotExists() {
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        when(saleService.getSaleByDateRange(anyString(), anyString()))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idSale));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/date-range")
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
                    assertEquals("Product not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void getSaleById_ShouldReturnSaleAndStatus200_WhenIdExists() {
        when(saleService.getSaleById(anyString())).thenReturn(sale);

        webTestClient.get()
                .uri("/api/v1/sales/{id}", idSale)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Sale retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getSaleById_ShouldThrowAndReturnStatus404_WhenIdDoesNotExist() {
        when(saleService.getSaleById(anyString())).thenThrow(new SalesNotFoundException(SALE_NOT_FOUND_ID + idSale));

        webTestClient.get()
                .uri("/api/v1/sales/{id}", idSale)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Sale not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });

    }

    @Test
    void getSaleById_ShouldThrowAndReturnStatus404_WhenProductOfResultNotExists() {
        when(saleService.getSaleById(anyString())).thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idSale));

        webTestClient.get()
                .uri("/api/v1/sales/{id}", idSale)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void generateExcelReport_ShouldReturnExcelFileAndStatus200_WhenSalesExist() {
        byte[] dummyExcelBytes = "ExcelDummyData".getBytes(StandardCharsets.UTF_8);
        when(saleService.getSaleByDateRange(anyString(), anyString())).thenReturn(sales);
        when(excelReportService.generateSalesReport(anyList())).thenReturn(Mono.just(dummyExcelBytes));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/excel")
                        .queryParam("startDate", "2023-01-01")
                        .queryParam("endDate", "2023-12-31")
                        .build())
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .expectHeader().valueEquals(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales_report.xlsx")
                .expectBody()
                .consumeWith(response ->{
                    byte[] responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertArrayEquals(dummyExcelBytes, responseBody);
                });
    }

    @Test
    void generateExcelReport_ShouldThrowAndReturnStatus404_WhenSaleNotExists() {
        when(saleService.getSaleByDateRange(anyString(), anyString()))
                .thenThrow(new SalesNotFoundException(SALE_NOT_FOUND_BETWEEN_DATES + "2023-01-01 and 2023-12-31"));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/excel")
                        .queryParam("startDate", "2023-01-01")
                        .queryParam("endDate", "2023-12-31")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No sales found between dates: 2023-01-01 and 2023-12-31", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void generateExcelReport_ShouldThrowAndReturnStatus404_WhenProductOfResultNotExists() {
        when(saleService.getSaleByDateRange(anyString(), anyString()))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idSale));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/excel")
                        .queryParam("startDate", "2023-01-01")
                        .queryParam("endDate", "2023-12-31")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void generateExcelReport_ShouldThrowAndReturnStatus500_WhenExcelGenerationFails() {
        when(saleService.getSaleByDateRange(anyString(), anyString())).thenReturn(sales);
        when(excelReportService.generateSalesReport(anyList()))
                .thenReturn(Mono.error(new ExcelReportGenerationException("Error generating the sales report")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/sales/excel")
                        .queryParam("startDate", "2023-01-01")
                        .queryParam("endDate", "2023-12-31")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(500, errorResponse.status());
                    assertEquals("Error generating the sales report", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void createSale_ShouldReturnCreatedSaleAndStatus201_WhenRequestIsValid() {
        when(saleService.createSale(any(CreateSaleRequest.class))).thenReturn(sale);

        webTestClient.post()
                .uri("/api/v1/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createSaleRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Sale created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void createSale_ShouldThrowAndReturnStatus404_WhenProductOfRequestNotExists() {
        when(saleService.createSale(any(CreateSaleRequest.class)))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idSale));

        webTestClient.post()
                .uri("/api/v1/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createSaleRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void updateSale_ShouldReturnUpdatedSaleAndStatus201_WhenSaleExists() {
        when(saleService.updateSale(anyString(), any(UpdateSaleRequest.class))).thenReturn(sale);

        webTestClient.put()
                .uri("/api/v1/sales/{id}", idSale)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateSaleRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Sale updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateSale_ShouldThrowAndReturnStatus404_WhenSaleDoesNotExist() {
        when(saleService.updateSale(anyString(), any(UpdateSaleRequest.class)))
                .thenThrow(new SalesNotFoundException(SALE_NOT_FOUND_ID + idSale));

        webTestClient.put()
                .uri("/api/v1/sales/{id}", idSale)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateSaleRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Sale not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void updateSale_ShouldThrowAndReturnStatus404_WhenProductOfRequestNotExists() {
        when(saleService.updateSale(anyString(), any(UpdateSaleRequest.class)))
                .thenThrow(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + idSale));

        webTestClient.put()
                .uri("/api/v1/sales/{id}", idSale)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateSaleRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Product not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void deleteSaleById_ShouldReturnVoidAndStatus204_WhenSaleExists() {
        when(saleService.deleteSaleById(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/sales/{id}", idSale)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));
    }

    @Test
    void deleteSaleById_ShouldThrowAndReturnStatus404_WhenSaleDoesNotExist() {
        when(saleService.deleteSaleById(anyString()))
                .thenThrow(new SalesNotFoundException(SALE_NOT_FOUND_ID + idSale));

        webTestClient.delete()
                .uri("/api/v1/sales/{id}", idSale)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Sale not found with id: " + idSale, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }
}