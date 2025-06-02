package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.RoleDTO;
import org.gad.inventory_service.dto.request.CreateRoleRequest;
import org.gad.inventory_service.dto.request.UpdateRoleRequest;
import org.gad.inventory_service.exception.RoleNotFoundException;
import org.gad.inventory_service.model.Permission;
import org.gad.inventory_service.model.Role;
import org.gad.inventory_service.repository.PermissionRepository;
import org.gad.inventory_service.repository.RoleRepository;
import org.gad.inventory_service.service.RoleService;
import org.gad.inventory_service.utils.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

import static org.gad.inventory_service.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Flux<RoleDTO> findAllRoles() {
        return roleRepository.findAll()
                .switchIfEmpty(Flux.error(new RoleNotFoundException(ROLES_NOT_FOUND)))
                .map(Mappers::roleToDTO)
                .doOnError(error -> log.error(ERROR_MESSAGE_FINDING_ROLES, error.getMessage()));
    }

    @Override
    public Mono<RoleDTO> findRoleById(String id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new RoleNotFoundException(ROLE_NOT_FOUND_ID + id)))
                .map(Mappers::roleToDTO)
                .doOnError(error -> log.error(ERROR_MESSAGE_ROLE_NOT_FOUND_ID, id, error.getMessage()));
    }

    @Override
    public Mono<RoleDTO> saveRole(CreateRoleRequest role) {
        Role roleToSave = Role.builder()
                .name(role.name())
                .permissions(convertStringToPermissions(role.permissions()))
                .build();

        return permissionRepository.findByNamesLikeIgnoreCase(role.permissions())
                .collectList()
                .flatMap(existingPermission -> {
                    if (existingPermission.isEmpty()) {
                        return Mono.error(new RoleNotFoundException("No permissions found for the provided names :" + role.permissions()));
                    }
                    return roleRepository.save(roleToSave);
                })
                .map(Mappers::roleToDTO)
                .doOnError(error -> log.error(ERROR_CREATING_ROLE, error.getMessage()));
    }

    @Override
    public Mono<RoleDTO> updateRole(String id, UpdateRoleRequest role) {
        return permissionRepository.findByNamesLikeIgnoreCase(role.permissions())
                .collectList()
                .flatMap(existingPermissions -> {
                    if (existingPermissions.isEmpty()) {
                        return Mono.error(new RoleNotFoundException("No permissions found for the provided names: " + role.permissions()));
                    }
                    return roleRepository.findById(id)
                            .switchIfEmpty(Mono.error(new RoleNotFoundException(ROLE_NOT_FOUND_ID + id)))
                            .flatMap(existingRole -> {
                                existingRole.setName(role.name());
                                existingRole.setPermissions(convertStringToPermissions(role.permissions()));
                                return roleRepository.save(existingRole);
                            });
                })
                .map(Mappers::roleToDTO)
                .doOnError(error -> log.error(ERROR_UPDATING_ROLE_BY_ID, id, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteRoleById(String id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new RoleNotFoundException(ROLE_NOT_FOUND_ID + id)))
                .flatMap(existingRole -> roleRepository.deleteById(existingRole.getIdRole()))
                .doOnError(error -> log.error(ERROR_DELETING_ROLE_BY_ID, id, error.getMessage()));
    }

    private Set<Permission> convertStringToPermissions(Set<String> permissions) {
        return permissions.stream()
                .map(permission -> Permission.builder().name(verifyRoleName(permission)).build())
                .collect(Collectors.toSet());
    }

    private String verifyRoleName(String roleName) {
        String formattedRoleName = roleName.trim().replaceAll("\\s+", "_").toUpperCase();
        if (formattedRoleName.startsWith(PREFIX_ROLE)) {
            return formattedRoleName;
        } else {
            return PREFIX_ROLE + formattedRoleName;
        }
    }
}
