package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class SaleRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private SaleRepository saleRepository;

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll().block();
        Sale sale = Sale.builder()
                .productId("product123")
                .quantity(10)
                .totalPrice(new BigDecimal("101.00"))
                .build();
        Sale sale2 = Sale.builder()
                .productId("product123test")
                .quantity(20)
                .totalPrice(new BigDecimal("199.00"))
                .build();
        saleRepository.saveAll(Flux.just(sale, sale2)).then().block();
    }

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void findSalesByProductId_ShouldReturnSales_WhenProductIdMatches() {
        var foundSales = saleRepository.findSalesByProductId("product123");

        StepVerifier.create(foundSales)
                .assertNext(sale -> {
                    assertNotNull(sale.getIdSale());
                    assertEquals("product123", sale.getProductId());
                    assertEquals(10, sale.getQuantity());
                    assertEquals(new BigDecimal("101.00"), sale.getTotalPrice());
                })
                .verifyComplete();
    }

    @Test
    void findSalesByProductId_ShouldReturnEmpty_WhenProductIdDoesNotMatch() {
        var foundSales = saleRepository.findSalesByProductId("nonexistent");

        StepVerifier.create(foundSales)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findByOptionalDateRange_ShouldReturnSales_WhenDateRangeMatches() {
        var startDate = LocalDateTime.now().minusDays(5);
        var endDate = LocalDateTime.now().plusDays(2);

        var foundSales = saleRepository.findByOptionalDateRange(startDate, endDate);

        StepVerifier.create(foundSales)
                .assertNext(sale -> {
                    assertNotNull(sale.getIdSale());
                    assertEquals("product123", sale.getProductId());
                    assertEquals(10, sale.getQuantity());
                    assertEquals(new BigDecimal("101.00"), sale.getTotalPrice());
                })
                .assertNext(sale -> {
                    assertNotNull(sale.getIdSale());
                    assertEquals("product123test", sale.getProductId());
                    assertEquals(20, sale.getQuantity());
                    assertEquals(new BigDecimal("199.00"), sale.getTotalPrice());
                })
                .verifyComplete();
    }

    @Test
    void findByOptionalDateRange_ShouldReturnEmpty_WhenNoSalesInDateRange() {
        var startDate = LocalDateTime.now().plusDays(10);
        var endDate = LocalDateTime.now().plusDays(20);

        var foundSales = saleRepository.findByOptionalDateRange(startDate, endDate);

        StepVerifier.create(foundSales)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findByOptionalPriceTotalRange_ShouldReturnSales_WhenPriceRangeMatches() {
        BigDecimal minPrice = new BigDecimal("100.00");
        BigDecimal maxPrice = new BigDecimal("200.00");

        var foundSales = saleRepository.findByOptionalPriceTotalRange(minPrice, maxPrice);

        StepVerifier.create(foundSales)
                .assertNext(sale -> {
                    assertNotNull(sale.getIdSale());
                    assertEquals("product123", sale.getProductId());
                    assertEquals(10, sale.getQuantity());
                    assertEquals(new BigDecimal("101.00"), sale.getTotalPrice());
                })
                .assertNext(sale -> {
                    assertNotNull(sale.getIdSale());
                    assertEquals("product123test", sale.getProductId());
                    assertEquals(20, sale.getQuantity());
                    assertEquals(new BigDecimal("199.00"), sale.getTotalPrice());
                })
                .verifyComplete();
    }

    @Test
    void findByOptionalPriceTotalRange_ShouldReturnEmpty_WhenNoSalesInPriceRange() {
        var minPrice = new BigDecimal("200.00");
        var maxPrice = new BigDecimal("300.00");

        var foundSales = saleRepository.findByOptionalPriceTotalRange(minPrice, maxPrice);

        StepVerifier.create(foundSales)
                .expectNextCount(0)
                .verifyComplete();
    }
}