package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Provider;
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
class ProviderRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private ProviderRepository providerRepository;

    @BeforeEach
    void setUp() {
        providerRepository.deleteAll().block();
        Provider provider = Provider.builder()
                .name("TechProvider")
                .ruc("12345678901")
                .dni("12345678")
                .address("123 Tech Street")
                .phone("123456789")
                .email("tech@mail.com")
                .build();
        providerRepository.save(provider).block();
    }

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void findProviderByNameContainingIgnoreCase_ShouldReturnProvider_WhenNameMatches() {
        var foundProvider = providerRepository.findProviderByNameContainingIgnoreCase("TechProvider");

        StepVerifier.create(foundProvider)
                .assertNext(provider -> {
                    assertNotNull(provider.getIdProvider());
                    assertEquals("TechProvider", provider.getName());
                    assertTrue(provider.isActive());
                })
                .verifyComplete();
    }

    @Test
    void findProviderByNameContainingIgnoreCase_ShouldReturnEmpty_WhenNameDoesNotMatch() {
        var foundProvider = providerRepository.findProviderByNameContainingIgnoreCase("NonExistentProvider");

        StepVerifier.create(foundProvider)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findProviderByEmail_ShouldReturnProvider_WhenEmailExists() {
        var foundProvider = providerRepository.findProviderByEmail("tech@mail.com");
        StepVerifier.create(foundProvider)
                .assertNext(provider -> {
                    assertNotNull(provider.getIdProvider());
                    assertEquals("TechProvider", provider.getName());
                    assertEquals("12345678901", provider.getRuc());
                    assertEquals("12345678", provider.getDni());
                    assertEquals("123 Tech Street", provider.getAddress());
                    assertEquals("123456789", provider.getPhone());
                    assertEquals("tech@mail.com", provider.getEmail());
                    assertTrue(provider.isActive());
                })
                .verifyComplete();
    }

    @Test
    void findProviderByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        var foundProvider = providerRepository.findProviderByEmail("nonExistentEmail@gmail.com");
        StepVerifier.create(foundProvider)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findProviderByRuc__ShouldReturnProvider_WhenRucExists() {
        var foundProvider = providerRepository.findProviderByRuc("12345678901");

        StepVerifier.create(foundProvider)
                .assertNext(provider -> {
                    assertNotNull(provider.getIdProvider());
                    assertEquals("TechProvider", provider.getName());
                    assertEquals("12345678901", provider.getRuc());
                    assertEquals("12345678", provider.getDni());
                    assertEquals("123 Tech Street", provider.getAddress());
                    assertEquals("123456789", provider.getPhone());
                    assertEquals("tech@mail.com", provider.getEmail());
                    assertTrue(provider.isActive());
                })
                .verifyComplete();
    }

    @Test
    void findProviderByRuc_ShouldReturnEmpty_WhenRucDoesNotExist() {
        var foundProvider = providerRepository.findProviderByRuc("00000000000");
        StepVerifier.create(foundProvider)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findProviderByDni_ShouldReturnProvider_WhenDniExists() {
        var foundProvider = providerRepository.findProviderByDni("12345678");
        StepVerifier.create(foundProvider)
                .assertNext(provider -> {
                    assertNotNull(provider.getIdProvider());
                    assertEquals("TechProvider", provider.getName());
                    assertEquals("12345678901", provider.getRuc());
                    assertEquals("12345678", provider.getDni());
                    assertEquals("123 Tech Street", provider.getAddress());
                    assertEquals("123456789", provider.getPhone());
                    assertEquals("tech@mail.com", provider.getEmail());
                    assertTrue(provider.isActive());
                })
                .verifyComplete();
    }

    @Test
    void findProviderByDni_ShouldReturnEmpty_WhenDniDoesNotExist() {
        var foundProvider = providerRepository.findProviderByDni("87654321");
        StepVerifier.create(foundProvider)
                .expectNextCount(0)
                .verifyComplete();
    }
}