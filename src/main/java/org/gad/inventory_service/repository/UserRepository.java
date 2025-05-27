package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.User;
import org.gad.inventory_service.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface UserRepository extends ReactiveMongoRepository<User, String>, UserRepositoryCustom {
    Mono<User> findUserByUsername(String username);
}
