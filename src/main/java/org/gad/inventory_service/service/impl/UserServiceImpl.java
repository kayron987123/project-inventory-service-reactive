package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.UserDTO;
import org.gad.inventory_service.dto.request.CreateUserRequest;
import org.gad.inventory_service.dto.request.UpdateUserRequest;
import org.gad.inventory_service.exception.RoleNotFoundException;
import org.gad.inventory_service.exception.UserNotFoundException;
import org.gad.inventory_service.model.User;
import org.gad.inventory_service.repository.RoleRepository;
import org.gad.inventory_service.repository.UserRepository;
import org.gad.inventory_service.service.UserService;
import org.gad.inventory_service.utils.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.gad.inventory_service.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Flux<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .switchIfEmpty(Flux.error(new UserNotFoundException(MESSAGE_USERS_NOT_FOUND_FLUX)))
                .map(Mappers::userToDTO)
                .doOnError(error -> log.error(ERROR_SEARCHING_USERS, error.getMessage()));
    }

    @Override
    public Mono<UserDTO> findUserByUsernameOrEmail(String username, String email) {
        return userRepository.findUserByUsernameEmail(username, email)
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_USERNAME + username + OR_EMAIL + email)))
                .map(Mappers::userToDTO)
                .doOnError(error -> log.error(ERROR_SEARCHING_USER_BY_USERNAME_EMAIL, error.getMessage()));
    }

    @Override
    public Flux<UserDTO> findUsersByNameOrLastName(String name, String lastName) {
        return userRepository.findUsersByNameLastName(name, lastName)
                .switchIfEmpty(Flux.error(new UserNotFoundException(USER_NOT_FOUND_NAME + name + AND_LAST_NAME + lastName)))
                .map(Mappers::userToDTO)
                .doOnError(error -> log.error(ERROR_SEARCHING_USERS_BY_NAME_LAST_NAME, error.getMessage()));
    }

    @Override
    public Mono<UserDTO> findUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_ID + id)))
                .map(Mappers::userToDTO)
                .doOnError(error -> log.error(ERROR_FINDING_USER_BY_ID, error.getMessage()));
    }

    @Override
    public Mono<UserDTO> createUSer(CreateUserRequest createUserRequest) {
        return roleRepository.findRoleByNameContainingIgnoreCase(DEFAULT_ROLE_FOR_NEW_USER)
                .switchIfEmpty(Mono.error(new RoleNotFoundException(DEFAULT_ROLE_NOT_FOUND + DEFAULT_ROLE_FOR_NEW_USER)))
                .map(role -> User.builder()
                        .username(createUserRequest.username())
                        .name(createUserRequest.name())
                        .lastName(createUserRequest.lastName())
                        .username(createUserRequest.username())
                        .password(passwordEncoder.encode(createUserRequest.password()))
                        .email(createUserRequest.email())
                        .phone(createUserRequest.phone())
                        .roles(Set.of(role))
                        .build())
                .flatMap(userRepository::save)
                .map(Mappers::userToDTO)
                .doOnError(error -> log.error(ERROR_CREATING_USER, error.getMessage()));
    }

    @Override
    public Mono<UserDTO> updateUser(String id, UpdateUserRequest updateUserRequest) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_ID + id)))
                .map(user -> {
                    user.setName(updateUserRequest.name());
                    user.setLastName(updateUserRequest.lastName());
                    user.setUsername(updateUserRequest.username());
                    user.setEmail(updateUserRequest.email());
                    user.setPhone(updateUserRequest.phone());
                    if (!passwordEncoder.matches(updateUserRequest.password(), user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(updateUserRequest.password()));
                    }
                    return user;
                })
                .flatMap(userRepository::save)
                .map(Mappers::userToDTO)
                .doOnError(error -> log.error(ERROR_UPDATING_USER, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_ID + id)))
                .flatMap(userRepository::delete)
                .doOnError(error -> log.error(ERROR_DELETING_USER, error.getMessage()));
    }
}
