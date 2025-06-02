package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.PermissionDTO;
import org.gad.inventory_service.dto.request.CreatePermissionRequest;
import org.gad.inventory_service.dto.request.UpdatePermissionRequest;
import org.gad.inventory_service.exception.PermissionNotFoundException;
import org.gad.inventory_service.model.Permission;
import org.gad.inventory_service.repository.PermissionRepository;
import org.gad.inventory_service.service.PermissionService;
import org.gad.inventory_service.utils.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public Flux<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll()
                .switchIfEmpty(Flux.error(new PermissionNotFoundException(PERMISSION_NOT_FOUND_FLUX)))
                .map(Mappers::permissionToDTO)
                .doOnError(error -> log.error(ERROR_SEARCHING_PERMISSIONS, error.getMessage()));
    }

    @Override
    public Mono<PermissionDTO> getPermissionById(String id) {
        return permissionRepository.findById(id)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException(PERMISSION_NOT_FOUND_ID + id)))
                .map(Mappers::permissionToDTO)
                .doOnError(error -> log.error(ERRORS_SEARCHING_PERMISSION_BY_ID, id, error.getMessage()));
    }

    @Override
    public Mono<PermissionDTO> createPermission(CreatePermissionRequest createPermissionRequest) {
        Permission permissionToSave = Permission.builder()
                .name(formatPermissionName(createPermissionRequest.name()))
                .build();

        return permissionRepository.save(permissionToSave)
                .map(Mappers::permissionToDTO)
                .doOnError(error -> log.error(ERROR_CREATING_PERMISSION, error.getMessage()));
    }

    @Override
    public Mono<PermissionDTO> updatePermission(String id, UpdatePermissionRequest updatePermissionRequest) {
        return permissionRepository.findById(id)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException(PERMISSION_NOT_FOUND_ID + id)))
                .flatMap(existingPermission -> {
                    existingPermission.setName(formatPermissionName(updatePermissionRequest.name()));
                    return permissionRepository.save(existingPermission);
                })
                .map(Mappers::permissionToDTO)
                .doOnError(error -> log.error(ERROR_UPDATING_PERMISSION, id, error.getMessage()));
    }

    @Override
    public Mono<Void> deletePermissionById(String id) {
        return permissionRepository.findById(id)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException(PERMISSION_NOT_FOUND_ID + id)))
                .flatMap(permission -> permissionRepository.deleteById(permission.getIdPermission()))
                .doOnError(error -> log.error(ERROR_DELETING_PERMISSION, id, error.getMessage()));
    }

    private String formatPermissionName(String name) {
        return name.trim().replaceAll("\\s+", "_").toUpperCase();
    }
}
