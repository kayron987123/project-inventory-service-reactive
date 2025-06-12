package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreateUserRequest;
import org.gad.inventory_service.dto.request.UpdateUserRequest;
import org.gad.inventory_service.exception.RoleNotFoundException;
import org.gad.inventory_service.exception.UserNotFoundException;
import org.gad.inventory_service.model.Role;
import org.gad.inventory_service.model.User;
import org.gad.inventory_service.repository.RoleRepository;
import org.gad.inventory_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.gad.inventory_service.utils.Constants.*;
import static org.gad.inventory_service.utils.Constants.OR_EMAIL;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .idUser("user-12345")
                .name("John")
                .lastName("Doe")
                .username("johndoe")
                .build();

        role = Role.builder()
                .idRole("rol-1")
                .name("ROLE_USER")
                .build();

        createUserRequest = CreateUserRequest.builder()
                .name("Jane")
                .lastName("Doe")
                .username("janedoe")
                .password("password123")
                .build();

        updateUserRequest = UpdateUserRequest.builder()
                .name("John")
                .lastName("Smith")
                .username("johnsmith")
                .build();
    }


    @Test
    void getAuthenticatedUser_ShouldReturnUserAuthenticatedDTO_WhenUserExists() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("johndoe");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByUsername("johndoe")).thenReturn(Mono.just(user));

        StepVerifier.create(
                        userService.getAuthenticatedUser()
                                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
                )
                .expectNextMatches(userDTO -> userDTO.idUser().equals(user.getIdUser()) &&
                        userDTO.name().equals(user.getName()) &&
                        userDTO.lastName().equals(user.getLastName()))
                .verifyComplete();

        verify(userRepository, times(1)).findUserByUsername("johndoe");
    }


    @Test
    void getAuthenticatedUser_ShouldReturnError_WhenUserDoesNotExist() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("johndoe");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByUsername("johndoe")).thenReturn(Mono.empty());

        StepVerifier.create(
                        userService.getAuthenticatedUser()
                                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
                )
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().equals(USER_NOT_FOUND_USERNAME + "johndoe"))
                .verify();

        verify(userRepository, times(1)).findUserByUsername("johndoe");
    }

    @Test
    void findAllUsers_ShouldReturnFluxOfUserDTO_WhenUsersExist() {
        when(userRepository.findAll()).thenReturn(Flux.just(user));

        StepVerifier.create(userService.findAllUsers())
                .expectNextMatches(userDTO -> userDTO.idUser().equals(user.getIdUser()) &&
                        userDTO.name().equals(user.getName()) &&
                        userDTO.lastName().equals(user.getLastName()))
                .verifyComplete();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findAllUsers_ShouldReturnError_WhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(userService.findAllUsers())
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().equals(MESSAGE_USERS_NOT_FOUND_FLUX))
                .verify();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findUserByUsernameOrEmail_ShouldReturnUserDTO_WhenUserExists() {
        when(userRepository.findUserByUsernameEmail(anyString(), anyString()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userService.findUserByUsernameOrEmail("johndoe", "johndoe@gmail.com"))
                .expectNextMatches(userDTO -> userDTO.idUser().equals(user.getIdUser()) &&
                        userDTO.name().equals(user.getName()) &&
                        userDTO.lastName().equals(user.getLastName()))
                .verifyComplete();
        verify(userRepository, times(1)).findUserByUsernameEmail(anyString(), anyString());
    }

    @Test
    void findUserByUsernameOrEmail_ShouldReturnError_WhenUserDoesNotExist() {
        when(userRepository.findUserByUsernameEmail(anyString(), anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(userService.findUserByUsernameOrEmail("unknown", "emailNonExistent@gmail.com"))
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().equals(USER_NOT_FOUND_USERNAME + "unknown" + OR_EMAIL + "emailNonExistent@gmail.com"))
                .verify();
        verify(userRepository, times(1)).findUserByUsernameEmail(anyString(), anyString());
    }

    @Test
    void findUsersByNameOrLastName_ShouldReturnFluxOfUserDTO_WhenUsersExist() {
        when(userRepository.findUsersByNameLastName(anyString(), anyString()))
                .thenReturn(Flux.just(user));

        StepVerifier.create(userService.findUsersByNameOrLastName("John", "Doe"))
                .expectNextMatches(userDTO -> userDTO.idUser().equals(user.getIdUser()) &&
                        userDTO.name().equals(user.getName()) &&
                        userDTO.lastName().equals(user.getLastName()))
                .verifyComplete();
        verify(userRepository, times(1)).findUsersByNameLastName(anyString(), anyString());
    }

    @Test
    void findUserById_ShouldReturnUserDTO_WhenUserExists() {
        when(userRepository.findById(anyString())).thenReturn(Mono.just(user));

        StepVerifier.create(userService.findUserById("user-12345"))
                .expectNextMatches(userDTO -> userDTO.idUser().equals(user.getIdUser()) &&
                        userDTO.name().equals(user.getName()) &&
                        userDTO.lastName().equals(user.getLastName()))
                .verifyComplete();
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void findUserById_ShouldReturnError_WhenUserDoesNotExist() {
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userService.findUserById("unknown-id"))
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().equals(USER_NOT_FOUND_ID + "unknown-id"))
                .verify();
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void createUser_ShouldReturnUserDTO_WhenRoleExists() {
        when(roleRepository.findRoleByNameContainingIgnoreCase(anyString())).thenReturn(Mono.just(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userService.createUSer(createUserRequest))
                .expectNextMatches(userDTO -> userDTO.idUser().equals(user.getIdUser()) &&
                        userDTO.name().equals(user.getName()) &&
                        userDTO.lastName().equals(user.getLastName()))
                .verifyComplete();
        verify(roleRepository, times(1)).findRoleByNameContainingIgnoreCase(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldReturnError_WhenRoleDoesNotExist() {
        when(roleRepository.findRoleByNameContainingIgnoreCase(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userService.createUSer(createUserRequest))
                .expectErrorMatches(throwable -> throwable instanceof RoleNotFoundException &&
                        throwable.getMessage().equals(DEFAULT_ROLE_NOT_FOUND + DEFAULT_ROLE_FOR_NEW_USER))
                .verify();
        verify(roleRepository, times(1)).findRoleByNameContainingIgnoreCase(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDTO_WhenUserExists() {
        when(userRepository.findById(anyString())).thenReturn(Mono.just(user));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userService.updateUser("user-12345", updateUserRequest))
                .expectNextMatches(userDTO -> userDTO.idUser().equals(user.getIdUser()) &&
                        userDTO.name().equals(updateUserRequest.name()) &&
                        userDTO.lastName().equals(updateUserRequest.lastName()))
                .verifyComplete();
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnError_WhenUserDoesNotExist() {
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userService.updateUser("unknown-id", updateUserRequest))
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().equals(USER_NOT_FOUND_ID + "unknown-id"))
                .verify();
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUserById_ShouldReturnMonoVoid_WhenUserExists() {
        when(userRepository.findById(anyString())).thenReturn(Mono.just(user));
        when(userRepository.deleteById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteUserById("user-12345"))
                .verifyComplete();
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteUserById_ShouldReturnError_WhenUserDoesNotExist() {
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteUserById("unknown-id"))
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().equals(USER_NOT_FOUND_ID + "unknown-id"))
                .verify();
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, never()).deleteById(anyString());
    }
}