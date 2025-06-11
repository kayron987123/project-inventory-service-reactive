package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(UserController.class)
@Import(TestConfig.class)
@WithMockUser
class UserControllerTest {
    @Autowired
    private UserService userService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void findAllUsers() {
    }

    @Test
    void findUserById() {
    }

    @Test
    void findUserByUsernameOrEmail() {
    }

    @Test
    void findUsersByNameOrLastName() {
    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUserById() {
    }
}