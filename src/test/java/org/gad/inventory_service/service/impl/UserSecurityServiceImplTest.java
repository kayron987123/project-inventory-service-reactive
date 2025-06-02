package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.model.User;
import org.gad.inventory_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSecurityServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSecurityServiceImpl userSecurityService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .idUser("12345")
                .name("John")
                .lastName("Doe")
                .username("johndoe")
                .build();
    }

    @Test
    void findByUsername_ShouldReturnUserDetails_WhenUserExists() {
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userSecurityService.findByUsername("johndoe"))
                .expectNextMatches(userDetails ->
                        userDetails.getUsername().equals("johndoe"))
                .verifyComplete();

        verify(userRepository, times(1)).findUserByUsername(anyString());
    }

    @Test
    void findByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(userSecurityService.findByUsername("nonexistent"))
                .expectErrorMatches(throwable ->
                        throwable instanceof UsernameNotFoundException &&
                                throwable.getMessage().equals("User not found with username: nonexistent"))
                .verify();

        verify(userRepository, times(1)).findUserByUsername(anyString());
    }
}