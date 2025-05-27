package org.gad.inventory_service.repository.custom;

import org.gad.inventory_service.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryCustom {
    Flux<User> findUsersByNameOrLastName(String name, String lastName);
    Mono<User> findUserByUsernameOrEmail(String username, String email);
}
