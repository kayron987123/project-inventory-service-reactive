package org.gad.inventory_service.controller;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateUserRequest;
import org.gad.inventory_service.dto.request.UpdateUserRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.UserService;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.gad.inventory_service.utils.Constants.*;
import static org.gad.inventory_service.utils.UtilsMethods.datetimeNowFormatted;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findAllUsers() {
        return userService.findAllUsers()
                .collectList()
                .map(user -> ResponseEntity.ok(DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_USERS_OK)
                        .data(user)
                        .timestamp(datetimeNowFormatted())
                        .build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> findUserById(@PathVariable String id) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_USER_OK)
                        .data(user)
                        .timestamp(datetimeNowFormatted())
                        .build())
                );
    }

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findUserByUsernameOrEmail(@RequestParam(required = false) String username,
                                                                        @RequestParam(required = false) String email) {
        return userService.findUserByUsernameOrEmail(username, email)
                .map(user -> ResponseEntity.ok(DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_USER_OK)
                        .data(user)
                        .timestamp(datetimeNowFormatted())
                        .build())
                );
    }

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findUsersByNameOrLastName(@RequestParam(required = false) String name,
                                                                        @RequestParam(required = false) String lastName) {
        return userService.findUsersByNameOrLastName(name, lastName)
                .collectList()
                .map(users -> ResponseEntity.ok(DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_USERS_OK)
                        .data(users)
                        .timestamp(datetimeNowFormatted())
                        .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return userService.createUSer(createUserRequest)
                .map(user -> {
                    URI location = UtilsMethods.createUri(USER_URI, user.idUser());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_USER_CREATED)
                            .data(user)
                            .timestamp(datetimeNowFormatted())
                            .build();

                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.updateUser(id, updateUserRequest)
                .map(user -> {
                    URI location = UtilsMethods.createUri(USER_URI, user.idUser());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message(MESSAGE_USER_UPDATED)
                            .data(user)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> deleteUserById(@PathVariable String id) {
        return userService.deleteUserById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
