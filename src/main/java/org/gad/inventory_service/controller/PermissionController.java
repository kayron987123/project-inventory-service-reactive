package org.gad.inventory_service.controller;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreatePermissionRequest;
import org.gad.inventory_service.dto.request.UpdatePermissionRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.gad.inventory_service.utils.Constants.*;
import static org.gad.inventory_service.utils.UtilsMethods.createUri;
import static org.gad.inventory_service.utils.UtilsMethods.datetimeNowFormatted;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> getAllPermissions() {
        return permissionService.getAllPermissions()
                .collectList()
                .map(permissions -> {
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message(MESSAGE_PERMISSIONS_OK)
                            .data(permissions)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.ok(dataResponse);
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> getPermissionById(@PathVariable String id) {
        return permissionService.getPermissionById(id)
                .map(permission -> {
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message(MESSAGE_PERMISSION_OK)
                            .data(permission)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.ok(dataResponse);
                });
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createPermission(@RequestBody CreatePermissionRequest createPermissionRequest) {
        return permissionService.createPermission(createPermissionRequest)
                .map(permission -> {
                    URI location = createUri(PERMISSION_URI, permission.idPermission());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_PERMISSION_CREATED)
                            .data(permission)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> updatePermission(@PathVariable String id,
                                                               @RequestBody UpdatePermissionRequest updatePermissionRequest) {
        return permissionService.updatePermission(id, updatePermissionRequest)
                .map(permission -> {
                    URI location = createUri(PERMISSION_URI, permission.idPermission());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_PERMISSION_UPDATED)
                            .data(permission)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> deletePermissionById(@PathVariable String id) {
        return permissionService.deletePermissionById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
