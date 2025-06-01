package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Brand;
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

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class BrandRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        brandRepository.deleteAll().block();
        Brand brand = Brand.builder()
                .name("Samsung")
                .build();
        brandRepository.save(brand).block();
    }

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void findBrandByNameContainingIgnoreCase_ShouldReturnBrand_WhenNameMatches() {
        Mono<Brand> foundBrand = brandRepository.findBrandByNameContainingIgnoreCase("Samsung");

        StepVerifier.create(foundBrand)
                .assertNext(brand -> {
                    assertNotNull(brand.getIdBrand());
                    assertEquals("Samsung", brand.getName());
                    assertTrue(brand.isActive());
                })
                .verifyComplete();
    }

    @Test
    void findBrandByNameContainingIgnoreCase_ShouldReturnEmpty_WhenNameDoesNotMatch() {
        Mono<Brand> foundBrand = brandRepository.findBrandByNameContainingIgnoreCase("Apple");

        StepVerifier.create(foundBrand)
                .expectNextCount(0)
                .verifyComplete();
    }
}