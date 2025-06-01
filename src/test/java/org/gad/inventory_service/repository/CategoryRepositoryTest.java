package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Category;
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
class CategoryRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll().block();
        Category category = Category.builder()
                .name("Electronics")
                .build();
        categoryRepository.save(category).block();
    }

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void findCategoryByNameContainingIgnoreCase_ShouldReturnCategory_WhenNameMatches() {
        var foundCategory = categoryRepository.findCategoryByNameContainingIgnoreCase("Electronics");

        StepVerifier.create(foundCategory)
                .assertNext(category -> {
                    assertNotNull(category.getIdCategory());
                    assertEquals("Electronics", category.getName());
                    assertTrue(category.isActive());
                })
                .verifyComplete();
    }

    @Test
    void findCategoryByNameContainingIgnoreCase_ShouldReturnEmpty_WhenNameDoesNotMatch() {
        var foundCategory = categoryRepository.findCategoryByNameContainingIgnoreCase("Home Appliances");

        StepVerifier.create(foundCategory)
                .expectNextCount(0)
                .verifyComplete();
    }
}