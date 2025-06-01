package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Permission;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class PermissionRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private PermissionRepository permissionRepository;

    @BeforeEach
    void setUp() {
        permissionRepository.deleteAll().block();
        Permission permission1 = Permission.builder()
                .name("CREATE_TEST")
                .build();
        Permission permission2 = Permission.builder()
                .name("READ_TEST")
                .build();
        permissionRepository.save(permission1).block();
        permissionRepository.save(permission2).block();
    }

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void findByNamesLikeIgnoreCase_ShouldReturnPermissions_WhenNamesMatch() {
        Set<String> names = Set.of("CREATE_TEST", "READ_TEST");
        var foundPermissions = permissionRepository.findByNamesLikeIgnoreCase(names);

        StepVerifier.create(foundPermissions)
                .assertNext(permissions -> {
                    assertNotNull(permissions);
                    assertEquals("CREATE_TEST", permissions.getName());
                })
                .assertNext(permissions -> {
                    assertNotNull(permissions);
                    assertEquals("READ_TEST", permissions.getName());
                })
                .verifyComplete();
    }

    @Test
    void findByNamesLikeIgnoreCase_ShouldReturnEmpty_WhenNoNamesMatch() {
        Set<String> names = Set.of("UPDATE_TEST", "DELETE_TEST");
        var foundPermissions = permissionRepository.findByNamesLikeIgnoreCase(names);

        StepVerifier.create(foundPermissions)
                .expectNextCount(0)
                .verifyComplete();
    }
}