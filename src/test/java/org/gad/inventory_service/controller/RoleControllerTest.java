package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.RoleDTO;
import org.gad.inventory_service.dto.request.CreateRoleRequest;
import org.gad.inventory_service.dto.request.UpdateRoleRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.gad.inventory_service.exception.RoleNotFoundException;
import org.gad.inventory_service.service.RoleService;
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

import java.util.Set;

import static org.gad.inventory_service.utils.Constants.ROLES_NOT_FOUND;
import static org.gad.inventory_service.utils.Constants.ROLE_NOT_FOUND_ID;
import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(RoleController.class)
@Import(TestConfig.class)
@WithMockUser
class RoleControllerTest {
    @Autowired
    private RoleService roleService;

    @Autowired
    private WebTestClient webTestClient;

    private final String idRole = "665c2e2f8b3e2a6b7c8d9e0f";
    private Flux<RoleDTO> roles;
    private Mono<RoleDTO> role;
    private CreateRoleRequest createRoleRequest;
    private UpdateRoleRequest updateRoleRequest;

    @BeforeEach
    void setUp() {
        roles = Flux.just(
                RoleDTO.builder()
                        .name("ROLE_TESTER")
                        .permissions(Set.of("READ", "WRITE"))
                        .build(),
                RoleDTO.builder()
                        .name("ROLE_ADMIN")
                        .build()
                );
        role = Mono.just(
                RoleDTO.builder()
                        .name("ROLE_TESTER")
                        .build()
        );
        createRoleRequest = CreateRoleRequest.builder()
                .name("ROLE_TESTER")
                .permissions(Set.of("READ", "WRITE"))
                .build();
        updateRoleRequest = UpdateRoleRequest.builder()
                .name("ROLE_TESTER")
                .permissions(Set.of("READ", "WRITE"))
                .build();
        Mockito.reset(roleService);
    }

    @Test
    void findAllRoles_ShouldReturnAllRolesAndStatus200_WhenRolesExist() {
        Mockito.when(roleService.findAllRoles()).thenReturn(roles);

        webTestClient.get()
                .uri("/api/v1/roles")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Roles retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findAllRoles_ShouldThrowAndReturnStatus404_WhenNoRolesExist() {
        Mockito.when(roleService.findAllRoles()).thenThrow(new RoleNotFoundException(ROLES_NOT_FOUND));

        webTestClient.get()
                .uri("/api/v1/roles")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Roles not found", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findRoleById_ShouldReturnRoleAndStatus200_WhenRoleExists() {
        Mockito.when(roleService.findRoleById(idRole)).thenReturn(role);

        webTestClient.get()
                .uri("/api/v1/roles/{id}", idRole)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Role retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findRoleById_ShouldThrowAndReturnStatus404_WhenRoleDoesNotExist() {
        Mockito.when(roleService.findRoleById(idRole)).thenThrow(new RoleNotFoundException(ROLE_NOT_FOUND_ID + idRole));

        webTestClient.get()
                .uri("/api/v1/roles/{id}", idRole)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Role not found with id: " + idRole, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void createRole_ShouldReturnCreatedRoleAndStatus201_WhenRoleIsCreated() {
        Mockito.when(roleService.saveRole(createRoleRequest)).thenReturn(role);

        webTestClient.post()
                .uri("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRoleRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Role created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateRole_ShouldReturnUpdatedRoleAndStatus201_WhenRoleIsUpdated() {
        Mockito.when(roleService.updateRole(idRole, updateRoleRequest)).thenReturn(role);

        webTestClient.put()
                .uri("/api/v1/roles/{id}", idRole)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRoleRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Role updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateRole_ShouldThrowAndReturnStatus404_WhenRoleDoesNotExist() {
        Mockito.when(roleService.updateRole(idRole, updateRoleRequest)).thenThrow(new RoleNotFoundException(ROLE_NOT_FOUND_ID + idRole));

        webTestClient.put()
                .uri("/api/v1/roles/{id}", idRole)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRoleRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Role not found with id: " + idRole, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void deleteRoleById_ShouldReturnVoidAndStatus204_WhenRoleIsDeleted() {
        Mockito.when(roleService.deleteRoleById(idRole)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/roles/{id}", idRole)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));

        Mockito.verify(roleService, Mockito.times(1)).deleteRoleById(idRole);
    }

    @Test
    void deleteRoleById_ShouldThrowAndReturnStatus404_WhenRoleDoesNotExist() {
        Mockito.when(roleService.deleteRoleById(idRole)).thenThrow(new RoleNotFoundException(ROLE_NOT_FOUND_ID + idRole));

        webTestClient.delete()
                .uri("/api/v1/roles/{id}", idRole)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Role not found with id: " + idRole, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }
}