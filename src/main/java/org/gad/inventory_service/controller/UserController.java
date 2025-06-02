package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    public Mono<ResponseEntity<DataResponse>> findUserById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                           @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_USER_OK)
                        .data(user)
                        .timestamp(datetimeNowFormatted())
                        .build())
                );
    }

    @GetMapping("/search/username-or-email")
    public Mono<ResponseEntity<DataResponse>> findUserByUsernameOrEmail(@RequestParam(required = false)
                                                                        @Pattern(regexp = REGEX_ONLY_TEST_AND_NUMBERS, message = MESSAGE_ONLY_TEST_AND_NUMBERS) String username,
                                                                        @RequestParam(required = false)
                                                                        @Email(message = INVALID_EMAIL_FORMAT) String email) {
        return userService.findUserByUsernameOrEmail(username, email)
                .map(user -> ResponseEntity.ok(DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_USER_OK)
                        .data(user)
                        .timestamp(datetimeNowFormatted())
                        .build())
                );
    }

    @GetMapping("/search/name-or-lastname")
    public Mono<ResponseEntity<DataResponse>> findUsersByNameOrLastName(@RequestParam(required = false)
                                                                        @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_NAME) String name,
                                                                        @RequestParam(required = false)
                                                                        @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_LAST_NAME) String lastName) {
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
    public Mono<ResponseEntity<DataResponse>> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
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
    public Mono<ResponseEntity<DataResponse>> updateUser(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                         @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id,
                                                         @RequestBody @Valid UpdateUserRequest updateUserRequest) {
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
    public Mono<ResponseEntity<DataResponse>> deleteUserById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                             @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return userService.deleteUserById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
