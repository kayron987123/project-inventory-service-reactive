package org.gad.inventory_service.controller;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateRoleRequest;
import org.gad.inventory_service.dto.request.UpdateRoleRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.RoleService;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.gad.inventory_service.utils.Constants.*;
import static org.gad.inventory_service.utils.UtilsMethods.datetimeNowFormatted;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findAllRoles() {
        return roleService.findAllRoles()
                .collectList()
                .map(roles ->
                        ResponseEntity.ok(
                                DataResponse.builder()
                                        .status(HttpStatus.OK.value())
                                        .message(MESSAGE_ROLES_OK)
                                        .data(roles)
                                        .timestamp(datetimeNowFormatted())
                                        .build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> findRoleById(@PathVariable String id) {
        return roleService.findRoleById(id)
                .map(role ->
                        ResponseEntity.ok(
                                DataResponse.builder()
                                        .status(HttpStatus.OK.value())
                                        .message(MESSAGE_ROLE_OK)
                                        .data(role)
                                        .timestamp(datetimeNowFormatted())
                                        .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createRole(@RequestBody CreateRoleRequest role) {
        return roleService.saveRole(role)
                .map(savedRole -> {
                    URI location = UtilsMethods.createUri(ROLE_URI, savedRole.idRole());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_ROLE_CREATED)
                            .data(savedRole)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> updateRole(@PathVariable String id, @RequestBody UpdateRoleRequest role) {
        return roleService.updateRole(id, role)
                .map(updatedRole -> {
                    URI location = UtilsMethods.createUri(ROLE_URI, updatedRole.idRole());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_ROLE_UPDATED)
                            .data(updatedRole)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> deleteRoleById(@PathVariable String id) {
        return roleService.deleteRoleById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
