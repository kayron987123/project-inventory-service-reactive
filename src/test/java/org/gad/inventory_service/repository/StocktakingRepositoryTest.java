package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Stocktaking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class StocktakingRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private StocktakingRepository stocktakingRepository;

    @BeforeEach
    void setUp() {
        stocktakingRepository.deleteAll().block();
        Stocktaking stocktaking = Stocktaking.builder()
                .productId("12345")
                .quantity(100)
                .performedBy("Tester")
                .build();
        stocktakingRepository.save(stocktaking).block();
    }

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void findStocktakingByProductId_ShouldReturnStocktaking_WhenProductIdMatches() {
        var foundStocktaking = stocktakingRepository.findStocktakingByProductId("12345");

        StepVerifier.create(foundStocktaking)
                .assertNext(stocktaking -> {
                    assertNotNull(stocktaking.getIdStocktaking());
                    assertEquals("12345", stocktaking.getProductId());
                    assertEquals(100, stocktaking.getQuantity());
                    assertEquals("Tester", stocktaking.getPerformedBy());
                })
                .verifyComplete();
    }

    @Test
    void findStocktakingByProductId_ShouldReturnEmpty_WhenProductIdDoesNotMatch() {
        var foundStocktaking = stocktakingRepository.findStocktakingByProductId("nonexistent");

        StepVerifier.create(foundStocktaking)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findByOptionalDateRange_ShouldReturnStocktaking_WhenDateRangeMatches() {
        var startDate = LocalDateTime.now().minusHours(5);
        var endDate = LocalDateTime.now().plusDays(1);

        var foundStocktakings = stocktakingRepository.findByOptionalDateRange(startDate, endDate);

        StepVerifier.create(foundStocktakings)
                .assertNext(stocktaking -> {
                    assertNotNull(stocktaking.getIdStocktaking());
                    assertEquals("12345", stocktaking.getProductId());
                    assertEquals(100, stocktaking.getQuantity());
                    assertEquals("Tester", stocktaking.getPerformedBy());
                })
                .verifyComplete();
    }

    @Test
    void findByOptionalDateRange_ShouldReturnEmpty_WhenNoStocktakingMatchesDateRange() {
        var startDate = LocalDateTime.now().plusHours(5);
        var endDate = LocalDateTime.now().plusDays(1);

        var foundStocktakings = stocktakingRepository.findByOptionalDateRange(startDate, endDate);

        StepVerifier.create(foundStocktakings)
                .expectNextCount(0)
                .verifyComplete();
    }
}