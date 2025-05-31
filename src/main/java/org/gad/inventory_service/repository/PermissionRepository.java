package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Permission;
import org.gad.inventory_service.repository.custom.PermissionRepositoryCustom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface PermissionRepository extends ReactiveMongoRepository<Permission, String>, PermissionRepositoryCustom {

}
