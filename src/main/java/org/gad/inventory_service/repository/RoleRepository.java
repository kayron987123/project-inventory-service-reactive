package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface RoleRepository extends ReactiveMongoRepository<Role, UUID> {
}
