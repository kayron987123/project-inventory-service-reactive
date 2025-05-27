package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface RoleRepository extends ReactiveMongoRepository<Role, String> {
    Mono<Role> findRoleByNameContainingIgnoreCase(String name);
}
