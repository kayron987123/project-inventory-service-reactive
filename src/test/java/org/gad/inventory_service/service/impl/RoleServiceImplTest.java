package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreateRoleRequest;
import org.gad.inventory_service.dto.request.UpdateRoleRequest;
import org.gad.inventory_service.exception.RoleNotFoundException;
import org.gad.inventory_service.model.Permission;
import org.gad.inventory_service.model.Role;
import org.gad.inventory_service.repository.PermissionRepository;
import org.gad.inventory_service.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.gad.inventory_service.utils.Constants.ROLES_NOT_FOUND;
import static org.gad.inventory_service.utils.Constants.ROLE_NOT_FOUND_ID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    Permission permission;
    Role role;
    CreateRoleRequest createRoleRequest;
    UpdateRoleRequest updateRoleRequest;

    @BeforeEach
    void setUp() {
        permission = Permission.builder()
                .idPermission("test-permission-id")
                .name("READ_PRIVILEGES")
                .build();

        role = Role.builder()
                .idRole("test-role-id")
                .name("TEST_ROLE")
                .permissions(Set.of())
                .build();

        createRoleRequest = CreateRoleRequest.builder()
                .name("Create Test Role")
                .permissions(Set.of("READ_PRIVILEGES", "WRITE_PRIVILEGES"))
                .build();

        updateRoleRequest = UpdateRoleRequest.builder()
                .name("Updated Test Role")
                .permissions(Set.of("READ_PRIVILEGES", "WRITE_PRIVILEGES"))
                .build();
    }

    @Test
    void findAllRoles_ShouldReturnFluxOfRoleDTOs_WhenRolesExist() {
        when(roleRepository.findAll())
                .thenReturn(Flux.just(role));

        StepVerifier.create(roleService.findAllRoles())
                .expectNextMatches(roleDTO ->
                        roleDTO.idRole().equals("test-role-id") &&
                                roleDTO.name().equals("TEST_ROLE"))
                .verifyComplete();

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void findAllRoles_ShouldReturnError_WhenNoRolesExist() {
        when(roleRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(roleService.findAllRoles())
                .expectErrorMatches(throwable ->
                        throwable instanceof RoleNotFoundException &&
                                throwable.getMessage().equals(ROLES_NOT_FOUND))
                .verify();

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void findRoleById_ShouldReturnRoleDTO_WhenRoleExists() {
        when(roleRepository.findById("test-role-id"))
                .thenReturn(Mono.just(role));

        StepVerifier.create(roleService.findRoleById("test-role-id"))
                .expectNextMatches(roleDTO ->
                        roleDTO.idRole().equals("test-role-id") &&
                                roleDTO.name().equals("TEST_ROLE"))
                .verifyComplete();

        verify(roleRepository, times(1)).findById("test-role-id");
    }

    @Test
    void findRoleById_ShouldReturnError_WhenRoleDoesNotExist() {
        when(roleRepository.findById("non-existent-id"))
                .thenReturn(Mono.empty());

        StepVerifier.create(roleService.findRoleById("non-existent-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof RoleNotFoundException &&
                                throwable.getMessage().equals(ROLE_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(roleRepository, times(1)).findById("non-existent-id");
    }

    @Test
    void saveRole_ShouldReturnRoleDTO_WhenRoleIsValid() {
        when(permissionRepository.findByNamesLikeIgnoreCase(createRoleRequest.permissions()))
                .thenReturn(Flux.just(permission));

        when(roleRepository.save(any(Role.class)))
                .thenReturn(Mono.just(role));

        StepVerifier.create(roleService.saveRole(createRoleRequest))
                .expectNextMatches(roleDTO ->
                        roleDTO.idRole().equals("test-role-id") &&
                                roleDTO.name().equals("TEST_ROLE"))
                .verifyComplete();

        verify(permissionRepository, times(1)).findByNamesLikeIgnoreCase(createRoleRequest.permissions());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void saveRole_ShouldReturnError_WhenPermissionsNotFound() {
        when(permissionRepository.findByNamesLikeIgnoreCase(createRoleRequest.permissions()))
                .thenReturn(Flux.empty());

        StepVerifier.create(roleService.saveRole(createRoleRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof RoleNotFoundException &&
                                throwable.getMessage().contains("No permissions found for the provided names"))
                .verify();

        verify(permissionRepository, times(1)).findByNamesLikeIgnoreCase(createRoleRequest.permissions());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void updateRole_ShouldReturnUpdatedRoleDTO_WhenRoleIsValid() {
        when(permissionRepository.findByNamesLikeIgnoreCase(updateRoleRequest.permissions()))
                .thenReturn(Flux.just(permission));

        when(roleRepository.findById(anyString()))
                .thenReturn(Mono.just(role));

        when(roleRepository.save(any(Role.class)))
                .thenReturn(Mono.just(role));

        StepVerifier.create(roleService.updateRole("test-role-id", updateRoleRequest))
                .expectNextMatches(roleDTO ->
                        roleDTO.idRole().equals("test-role-id") &&
                                roleDTO.name().equals("Updated Test Role"))
                .verifyComplete();

        verify(permissionRepository, times(1)).findByNamesLikeIgnoreCase(updateRoleRequest.permissions());
        verify(roleRepository, times(1)).findById("test-role-id");
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void updateRole_ShouldReturnError_WhenPermissionsNotFound() {
        when(permissionRepository.findByNamesLikeIgnoreCase(updateRoleRequest.permissions()))
                .thenReturn(Flux.empty());

        StepVerifier.create(roleService.updateRole("test-role-id", updateRoleRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof RoleNotFoundException &&
                                throwable.getMessage().contains("No permissions found for the provided names"))
                .verify();

        verify(permissionRepository, times(1)).findByNamesLikeIgnoreCase(updateRoleRequest.permissions());
        verify(roleRepository, never()).findById(anyString());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void updateRole_ShouldReturnError_WhenRoleDoesNotExist() {
        when(permissionRepository.findByNamesLikeIgnoreCase(updateRoleRequest.permissions()))
                .thenReturn(Flux.just(permission));

        when(roleRepository.findById("non-existent-id"))
                .thenReturn(Mono.empty());

        StepVerifier.create(roleService.updateRole("non-existent-id", updateRoleRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof RoleNotFoundException &&
                                throwable.getMessage().equals(ROLE_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(permissionRepository, times(1)).findByNamesLikeIgnoreCase(updateRoleRequest.permissions());
        verify(roleRepository, times(1)).findById("non-existent-id");
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void deleteRoleById_ShouldDeleteRole_WhenRoleExists() {
        when(roleRepository.findById("test-role-id"))
                .thenReturn(Mono.just(role));

        when(roleRepository.deleteById("test-role-id"))
                .thenReturn(Mono.empty());

        StepVerifier.create(roleService.deleteRoleById("test-role-id"))
                .verifyComplete();

        verify(roleRepository, times(1)).findById("test-role-id");
        verify(roleRepository, times(1)).deleteById("test-role-id");
    }

    @Test
    void deleteRoleById_ShouldReturnError_WhenRoleDoesNotExist() {
        when(roleRepository.findById("non-existent-id"))
                .thenReturn(Mono.empty());

        StepVerifier.create(roleService.deleteRoleById("non-existent-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof RoleNotFoundException &&
                                throwable.getMessage().equals(ROLE_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(roleRepository, times(1)).findById("non-existent-id");
        verify(roleRepository, never()).deleteById(anyString());
    }
}