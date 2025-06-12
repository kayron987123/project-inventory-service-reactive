package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.UserDTO;
import org.gad.inventory_service.dto.request.CreateUserRequest;
import org.gad.inventory_service.dto.request.UpdateUserRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.gad.inventory_service.exception.UserNotFoundException;
import org.gad.inventory_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;
import static org.gad.inventory_service.utils.Constants.OR_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
@Import(TestConfig.class)
@WithMockUser
class UserControllerTest {
    @Autowired
    private UserService userService;

    @Autowired
    private WebTestClient webTestClient;

    private final String idUser = "665c2e2f8b3e2a6b7c8d9e0f";
    private Flux<UserDTO> users;
    private Mono<UserDTO> user;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        users = Flux.just(
                UserDTO.builder()
                        .name("John")
                        .lastName("Doe")
                        .build()
                ,
                UserDTO.builder()
                        .name("Jane")
                        .lastName("Doe")
                        .build());
        user = Mono.just(
                UserDTO.builder()
                        .name("John")
                        .lastName("Doe")
                        .build()
        );
        createUserRequest = CreateUserRequest.builder()
                .name("John")
                .lastName("Doe")
                .username("johndoe")
                .password("password123")
                .email("doe@gmail.com")
                .phone("987654321")
                .build();
        updateUserRequest = UpdateUserRequest.builder()
                .name("John")
                .lastName("Doe")
                .username("johndoe")
                .password("newpassword123")
                .email("doe2@gmail.com")
                .phone("987654321")
                .build();
        Mockito.reset(userService);
    }

    @Test
    void findAllUsers_ShouldReturnAllUsersAndStatus200_WhenUsersExist() {
        when(userService.findAllUsers()).thenReturn(users);

        webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Users retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findAllUsers_ShouldThrowAndReturnStatus404_WhenNoUsersExist() {
        when(userService.findAllUsers()).thenThrow(new UserNotFoundException(MESSAGE_USERS_NOT_FOUND_FLUX));

        webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No users found", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findUserById_ShouldReturnUserAndStatus200_WhenUserExists() {
        when(userService.findUserById(anyString())).thenReturn(user);

        webTestClient.get()
                .uri("/api/v1/users/{id}", idUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("User retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findUserById_ShouldThrowAndReturnStatus404_WhenUserDoesNotExist() {
        when(userService.findUserById(anyString())).thenThrow(new UserNotFoundException(USER_NOT_FOUND_ID + idUser));

        webTestClient.get()
                .uri("/api/v1/users/{id}", idUser)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("User not found with id: " + idUser, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findUserByUsernameOrEmail_ShouldReturnUserAndStatus200_WhenUserExists() {
        String username = "johndoe";
        String email = "jhon@gmail.com";

        when(userService.findUserByUsernameOrEmail(anyString(), anyString())).thenReturn(user);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users/search/username-or-email")
                        .queryParam("username", username)
                        .queryParam("email", email)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("User retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findUserByUsernameOrEmail_ShouldThrowAndReturnStatus404_WhenUserDoesNotExist() {
        String username = "johndoe";
        String email = "not@gmail.com";

        when(userService.findUserByUsernameOrEmail(anyString(), anyString()))
                .thenThrow(new UserNotFoundException(USER_NOT_FOUND_USERNAME + username + OR_EMAIL + email));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users/search/username-or-email")
                        .queryParam("username", username)
                        .queryParam("email", email)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("User not found with username: " + username + " or email: " + email, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findUsersByNameOrLastName_ShouldReturnUsersAndStatus200_WhenUsersExist() {
        String name = "John";
        String lastName = "Doe";

        when(userService.findUsersByNameOrLastName(anyString(), anyString())).thenReturn(users);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users/search/name-or-lastname")
                        .queryParam("name", name)
                        .queryParam("lastName", lastName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Users retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findUsersByNameOrLastName_ShouldThrowAndReturnStatus404_WhenNoUsersExist() {
        String name = "NonExistent";
        String lastName = "User";

        when(userService.findUsersByNameOrLastName(anyString(), anyString()))
                .thenThrow(new UserNotFoundException(USER_NOT_FOUND_NAME + name + AND_LAST_NAME + lastName));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users/search/name-or-lastname")
                        .queryParam("name", name)
                        .queryParam("lastName", lastName)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("User not found with name: " + name + " and last name: " + lastName, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void createUser_ShouldReturnCreatedUserAndStatus201_WhenUserIsCreated() {
        when(userService.createUSer(Mockito.any(CreateUserRequest.class))).thenReturn(user);

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("User created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void createUser_ShouldThrowAndReturnStatus404_WhenRoleNotFound() {
        when(userService.createUSer(Mockito.any(CreateUserRequest.class)))
                .thenThrow(new UserNotFoundException(DEFAULT_ROLE_NOT_FOUND + DEFAULT_ROLE_FOR_NEW_USER));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Default role not found: " + DEFAULT_ROLE_FOR_NEW_USER, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserAndStatus200_WhenUserIsUpdated() {
        when(userService.updateUser(anyString(), Mockito.any(UpdateUserRequest.class))).thenReturn(user);

        webTestClient.put()
                .uri("/api/v1/users/{id}", idUser)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateUserRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("User updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateUser_ShouldThrowAndReturnStatus404_WhenUserNotFound() {
        when(userService.updateUser(anyString(), Mockito.any(UpdateUserRequest.class)))
                .thenThrow(new UserNotFoundException(USER_NOT_FOUND_ID + idUser));

        webTestClient.put()
                .uri("/api/v1/users/{id}", idUser)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateUserRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("User not found with id: " + idUser, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void deleteUserById_ShouldReturnVoidAndStatus204_WhenUserIsDeleted() {
        when(userService.deleteUserById(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/users/{id}", idUser)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));
    }

    @Test
    void deleteUserById_ShouldThrowAndReturnStatus404_WhenUserNotFound() {
        when(userService.deleteUserById(anyString()))
                .thenThrow(new UserNotFoundException(USER_NOT_FOUND_ID + idUser));

        webTestClient.delete()
                .uri("/api/v1/users/{id}", idUser)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("User not found with id: " + idUser, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }
}