package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.UserAuthenticatedDTO;
import org.gad.inventory_service.dto.UserDTO;
import org.gad.inventory_service.dto.request.CreateUserRequest;
import org.gad.inventory_service.dto.request.UpdateUserRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserAuthenticatedDTO> getAuthenticatedUser();
    Flux<UserDTO> findAllUsers();
    Mono<UserDTO> findUserByUsernameOrEmail(String username, String email);
    Flux<UserDTO> findUsersByNameOrLastName(String name, String lastName);
    Mono<UserDTO> findUserById(String id);
    Mono<UserDTO> createUSer(CreateUserRequest createUserRequest);
    Mono<UserDTO> updateUser(String id, UpdateUserRequest updateUserRequest);
    Mono<Void> deleteUserById(String id);
}
