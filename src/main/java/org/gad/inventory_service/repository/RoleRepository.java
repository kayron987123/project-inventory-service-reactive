package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface RoleRepository extends ReactiveMongoRepository<Role, String> {
}
