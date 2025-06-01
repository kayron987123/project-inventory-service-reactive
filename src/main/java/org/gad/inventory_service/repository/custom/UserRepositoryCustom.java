package org.gad.inventory_service.repository.custom;

import org.gad.inventory_service.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryCustom {
    Flux<User> findUsersByNameLastName(String name, String lastName);
    Mono<User> findUserByUsernameEmail(String username, String email);
}
