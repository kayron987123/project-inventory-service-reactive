package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreatePermissionRequest;
import org.gad.inventory_service.dto.request.UpdatePermissionRequest;
import org.gad.inventory_service.exception.PermissionNotFoundException;
import org.gad.inventory_service.model.Permission;
import org.gad.inventory_service.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.gad.inventory_service.utils.Constants.PERMISSION_NOT_FOUND_FLUX;
import static org.gad.inventory_service.utils.Constants.PERMISSION_NOT_FOUND_ID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceImplTest {
    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private Permission permission;
    private CreatePermissionRequest createPermissionRequest;
    private UpdatePermissionRequest updatePermissionRequest;

    @BeforeEach
    void setUp() {
        permission = Permission.builder()
                .idPermission("permission-1")
                .name("READ_PRIVILEGES")
                .build();
        createPermissionRequest = CreatePermissionRequest.builder()
                .name("READ_PRIVILEGES")
                .build();
        updatePermissionRequest = UpdatePermissionRequest.builder()
                .name("WRITE_PRIVILEGES")
                .build();
    }

    @Test
    void getAllPermissions_ShouldReturnFluxOfPermissionDTO_WhenPermissionsExist() {
        when(permissionRepository.findAll())
                .thenReturn(Flux.just(permission));

        StepVerifier.create(permissionService.getAllPermissions())
                .expectNextMatches(dto ->
                        dto.idPermission().equals("permission-1") &&
                                dto.name().equals("READ_PRIVILEGES"))
                .verifyComplete();

        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    void getAllPermissions_ShouldThrow_WhenPermissionsIsEmpty() {
        when(permissionRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(permissionService.getAllPermissions())
                .expectErrorMatches(throwable ->
                        throwable instanceof PermissionNotFoundException &&
                                throwable.getMessage().equals(PERMISSION_NOT_FOUND_FLUX))
                .verify();

        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    void getPermissionById_ShouldReturnPermissionDTO_WhenPermissionExists() {
        when(permissionRepository.findById(anyString()))
                .thenReturn(Mono.just(permission));

        StepVerifier.create(permissionService.getPermissionById("permission-1"))
                .expectNextMatches(dto ->
                        dto.idPermission().equals("permission-1") &&
                                dto.name().equals("READ_PRIVILEGES"))
                .verifyComplete();
        verify(permissionRepository, times(1)).findById(anyString());
    }

    @Test
    void getPermissionById_ShouldThrow_WhenPermissionDoesNotExist() {
        when(permissionRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(permissionService.getPermissionById("non-existent-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof PermissionNotFoundException &&
                                throwable.getMessage().equals(PERMISSION_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(permissionRepository, times(1)).findById(anyString());
    }

    @Test
    void createPermission_ShouldReturnPermissionDTO_WhenPermissionIsCreated() {
        when(permissionRepository.save(any(Permission.class)))
                .thenReturn(Mono.just(permission));

        StepVerifier.create(permissionService.createPermission(createPermissionRequest))
                .expectNextMatches(dto ->
                        dto.idPermission().equals("permission-1") &&
                                dto.name().equals("READ_PRIVILEGES"))
                .verifyComplete();

        verify(permissionRepository, times(1)).save(any(Permission.class));
    }

    @Test
    void updatePermission_ShouldReturnUpdatedPermissionDTO_WhenPermissionExists() {
        when(permissionRepository.findById(anyString()))
                .thenReturn(Mono.just(permission));
        when(permissionRepository.save(any(Permission.class)))
                .thenReturn(Mono.just(permission));

        StepVerifier.create(permissionService.updatePermission("permission-1", updatePermissionRequest))
                .expectNextMatches(dto ->
                        dto.idPermission().equals("permission-1") &&
                                dto.name().equals("WRITE_PRIVILEGES"))
                .verifyComplete();

        verify(permissionRepository, times(1)).findById(anyString());
        verify(permissionRepository, times(1)).save(any(Permission.class));
    }

    @Test
    void updatePermission_ShouldThrow_WhenPermissionDoesNotExist() {
        when(permissionRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(permissionService.updatePermission("non-existent-id", updatePermissionRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof PermissionNotFoundException &&
                                throwable.getMessage().equals(PERMISSION_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(permissionRepository, times(1)).findById(anyString());
    }

    @Test
    void deletePermissionById_ShouldDeletePermission_WhenPermissionExists() {
        when(permissionRepository.findById(anyString()))
                .thenReturn(Mono.just(permission));
        when(permissionRepository.deleteById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(permissionService.deletePermissionById("permission-1"))
                .verifyComplete();

        verify(permissionRepository, times(1)).findById(anyString());
        verify(permissionRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deletePermissionById_ShouldThrow_WhenPermissionDoesNotExist() {
        when(permissionRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(permissionService.deletePermissionById("non-existent-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof PermissionNotFoundException &&
                                throwable.getMessage().equals(PERMISSION_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(permissionRepository, times(1)).findById(anyString());
    }
}