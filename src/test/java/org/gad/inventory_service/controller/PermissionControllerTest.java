package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.PermissionDTO;
import org.gad.inventory_service.dto.request.CreatePermissionRequest;
import org.gad.inventory_service.dto.request.UpdatePermissionRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.exception.PermissionNotFoundException;
import org.gad.inventory_service.service.PermissionService;
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

import static org.gad.inventory_service.utils.Constants.PERMISSION_NOT_FOUND_FLUX;
import static org.gad.inventory_service.utils.Constants.PERMISSION_NOT_FOUND_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@WebFluxTest(PermissionController.class)
@Import(TestConfig.class)
@WithMockUser
class PermissionControllerTest {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private WebTestClient webTestClient;

    private final String idPermission = "665c2e2f8b3e2a6b7c8d9e0f";
    private Flux<PermissionDTO> permissions;
    private Mono<PermissionDTO> permission;
    private CreatePermissionRequest createPermissionRequest;
    private UpdatePermissionRequest updatePermissionRequest;

    @BeforeEach
    void setUp() {
        permissions = Flux.just(
                PermissionDTO.builder()
                        .name("CREATE_TEST")
                        .build(),
                PermissionDTO.builder()
                        .name("READ_TEST")
                        .build());

        permission = Mono.just(
                PermissionDTO.builder()
                        .name("UPDATE_TEST")
                        .build());

        createPermissionRequest = CreatePermissionRequest.builder()
                .name("CREATE_TEST")
                .build();

        updatePermissionRequest = UpdatePermissionRequest.builder()
                .name("UPDATE_TEST")
                .build();
        Mockito.reset(permissionService);
    }

    @Test
    void getAllPermissions_ShouldReturnAllPermissionsAndStatus200_WhenPermissionsExist() {
        Mockito.when(permissionService.getAllPermissions()).thenReturn(permissions);

        webTestClient.get()
                .uri("/api/v1/permissions")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Permissions retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getAllPermissions_ShouldThrowAndReturnStatus404_WhenPermissionsDoNotExist() {
        Mockito.when(permissionService.getAllPermissions())
                        .thenThrow(new PermissionNotFoundException(PERMISSION_NOT_FOUND_FLUX));

        webTestClient.get()
                .uri("/api/v1/permissions")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Permissions not found", dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getPermissionById_ShouldReturnPermissionAndStatus200_WhenPermissionExists() {
        Mockito.when(permissionService.getPermissionById(anyString()))
                .thenReturn(permission);

        webTestClient.get()
                .uri("/api/v1/permissions/{id}", idPermission)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Permission retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void getPermissionById_ShouldThrowAndReturnStatus404_WhenPermissionDoesNotExist() {
        Mockito.when(permissionService.getPermissionById(anyString()))
                .thenThrow(new PermissionNotFoundException(PERMISSION_NOT_FOUND_ID + idPermission));

        webTestClient.get()
                .uri("/api/v1/permissions/{id}", idPermission)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Permission not found with id: " + idPermission, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void createPermission_ShouldReturnCreatedPermissionAndStatus201_WhenPermissionIsCreated() {
        Mockito.when(permissionService.createPermission(Mockito.any(CreatePermissionRequest.class)))
                .thenReturn(permission);

        webTestClient.post()
                .uri("/api/v1/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createPermissionRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Permission created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updatePermission_ShouldReturnUpdatedPermissionAndStatus201_WhenPermissionExists() {
        Mockito.when(permissionService.updatePermission(anyString(), Mockito.any(UpdatePermissionRequest.class)))
                .thenReturn(permission);

        webTestClient.put()
                .uri("/api/v1/permissions/{id}", idPermission)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatePermissionRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Permission updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updatePermission_ShouldThrowAndReturnStatus404_WhenPermissionDoesNotExist() {
        Mockito.when(permissionService.updatePermission(anyString(), Mockito.any(UpdatePermissionRequest.class)))
                .thenThrow(new PermissionNotFoundException(PERMISSION_NOT_FOUND_ID + idPermission));

        webTestClient.put()
                .uri("/api/v1/permissions/{id}", idPermission)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatePermissionRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Permission not found with id: " + idPermission, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void deletePermissionById_ShouldReturnNoContentAndStatus204_WhenPermissionExists() {
        Mockito.when(permissionService.deletePermissionById(anyString()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/permissions/{id}", idPermission)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));
    }

    @Test
    void deletePermissionById_ShouldThrowAndReturnStatus404_WhenPermissionDoesNotExist() {
        Mockito.when(permissionService.deletePermissionById(anyString()))
                .thenThrow(new PermissionNotFoundException(PERMISSION_NOT_FOUND_ID + idPermission));

        webTestClient.delete()
                .uri("/api/v1/permissions/{id}", idPermission)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(404, dataResponse.status());
                    assertEquals("Permission not found with id: " + idPermission, dataResponse.message());
                    assertNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }
}