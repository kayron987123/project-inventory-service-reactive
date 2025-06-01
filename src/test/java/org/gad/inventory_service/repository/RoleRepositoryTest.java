package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Permission;
import org.gad.inventory_service.model.Role;
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
class RoleRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll().block();
        Permission permission = Permission.builder()
                .name("CRUD_TESTS")
                .build();
        Role role = Role.builder()
                .name("ROLE_ADMIN")
                .permissions(Set.of(permission))
                .build();
        permissionRepository.save(permission).block();
        roleRepository.save(role).block();
    }

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void findRoleByNameContainingIgnoreCase_ShouldReturnRole_WhenNameMatches() {
        var foundRole = roleRepository.findRoleByNameContainingIgnoreCase("ROLE_ADMIN");

        StepVerifier.create(foundRole)
                .assertNext(role -> {
                    assertNotNull(role.getIdRole());
                    assertEquals("ROLE_ADMIN", role.getName());
                    assertTrue(role.isActive());
                    assertFalse(role.getPermissions().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void findRoleByNameContainingIgnoreCase_ShouldReturnEmpty_WhenNameDoesNotMatch() {
        var foundRole = roleRepository.findRoleByNameContainingIgnoreCase("ROLE_USER");

        StepVerifier.create(foundRole)
                .expectNextCount(0)
                .verifyComplete();
    }
}