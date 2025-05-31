package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.PermissionDTO;
import org.gad.inventory_service.dto.request.CreatePermissionRequest;
import org.gad.inventory_service.dto.request.UpdatePermissionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PermissionService {
    Flux<PermissionDTO> getAllPermissions();
    Mono<PermissionDTO> getPermissionById(String id);
    Mono<PermissionDTO> createPermission(CreatePermissionRequest createPermissionRequest);
    Mono<PermissionDTO> updatePermission(String id, UpdatePermissionRequest updatePermissionRequest);
    Mono<Void> deletePermissionById(String id);
}
