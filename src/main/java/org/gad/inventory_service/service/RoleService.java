package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.RoleDTO;
import org.gad.inventory_service.dto.request.CreateRoleRequest;
import org.gad.inventory_service.dto.request.UpdateRoleRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleService {
    Flux<RoleDTO> findAllRoles();
    Mono<RoleDTO> findRoleById(String id);
    Mono<RoleDTO> saveRole(CreateRoleRequest role);
    Mono<RoleDTO> updateRole(String id, UpdateRoleRequest role);
    Mono<Void> deleteRoleById(String id);
}
