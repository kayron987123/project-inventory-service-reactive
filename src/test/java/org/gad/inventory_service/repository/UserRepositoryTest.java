package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Role;
import org.gad.inventory_service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class UserRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll().block();
        User user = User.builder()
                .name("Test")
                .lastName("User")
                .username("testuser")
                .password("password123")
                .email("email@mail.com")
                .phone("123456789")
                .roles(Set.of(Role.builder().name("ROLE_USER").build()))
                .build();
        userRepository.save(user).block();
    }

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void findUserByUsername_ShouldReturnUser_WhenUsernameMatches() {
        var foundUser = userRepository.findUserByUsername("testuser");

        StepVerifier.create(foundUser)
                .assertNext(user -> {
                    assertNotNull(user.getIdUser());
                    assertEquals("Test", user.getName());
                    assertEquals("User", user.getLastName());
                    assertEquals("testuser", user.getUsername());
                    assertTrue(user.getIsActive());
                })
                .verifyComplete();
    }

    @Test
    void findUserByUsername_ShouldReturnEmpty_WhenUsernameDoesNotMatch() {
        var foundUser = userRepository.findUserByUsername("nonexistentuser");

        StepVerifier.create(foundUser)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findUsersByNameOrLastName_ShouldReturnUsers_WhenNameOrLastNameMatches() {
        var foundUsers = userRepository.findUsersByNameLastName("Test", "User");

        StepVerifier.create(foundUsers)
                .assertNext(user -> {
                    assertNotNull(user.getIdUser());
                    assertEquals("Test", user.getName());
                    assertEquals("User", user.getLastName());
                    assertEquals("testuser", user.getUsername());
                })
                .verifyComplete();
    }

    @Test
    void findUsersByNameOrLastName_ShouldReturnEmpty_WhenNoMatch() {
        var foundUsers = userRepository.findUsersByNameLastName("NonexistentName", "NonexistentLastName");

        StepVerifier.create(foundUsers)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findUserByUsernameEmail_ShouldReturnUser_WhenUsernameAndEmailMatch() {
        var foundUser = userRepository.findUserByUsernameEmail("testuser", "email@mail.com");
        StepVerifier.create(foundUser)
                .assertNext(user -> {
                    assertNotNull(user.getIdUser());
                    assertEquals("Test", user.getName());
                    assertEquals("User", user.getLastName());
                    assertEquals("testuser", user.getUsername());
                    assertEquals("email@mail.com", user.getEmail());
                })
                .verifyComplete();
    }

    @Test
    void findUserByUsernameEmail_ShouldReturnEmpty_WhenUsernameOrEmailDoesNotMatch() {
        var foundUser = userRepository.findUserByUsernameEmail("NonexistentUsername", "NonExistentEmail");
        StepVerifier.create(foundUser)
                .expectNextCount(0)
                .verifyComplete();
    }
}