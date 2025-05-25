package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Permission;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface PermissionRepository extends ReactiveMongoRepository<Permission, UUID> {
}
