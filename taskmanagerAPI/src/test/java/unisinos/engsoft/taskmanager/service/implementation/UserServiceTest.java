package unisinos.engsoft.taskmanager.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.DTO.CreateUserRequest;
import unisinos.engsoft.taskmanager.DTO.PutUserRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;
import unisinos.engsoft.taskmanager.service.interfaces.IPasswordEncryptionService;
import unisinos.engsoft.taskmanager.service.interfaces.IUserValidation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private IPasswordEncryptionService passwordEncryptionService;
    private IUserValidation userValidation;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncryptionService = Mockito.mock(IPasswordEncryptionService.class);
        userValidation = Mockito.mock(IUserValidation.class);
        userService = new UserService(userRepository, passwordEncryptionService, userValidation);
    }

    @Test
    void createUser_success() {
        CreateUserRequest request = new CreateUserRequest(
                "First", "Last", "user@example.com", "password"
        );

        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncryptionService.encrypt("password")).thenReturn("encryptedPassword");

        ResponseEntity<UserDTO> response = userService.createUser(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("user@example.com", response.getBody().getEmail());

        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void createUser_emailAlreadyExists_throwsConflict() {
        CreateUserRequest request = new CreateUserRequest(
                "First", "Last", "user@example.com", "password"
        );

        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(request));

        assertEquals("409 CONFLICT \"email already in use\"", exception.getMessage());
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void getUserById_success() {
        Users user = new Users();
        user.setEmail("user@example.com");

        when(userValidation.validateUserById(1)).thenReturn(user);

        ResponseEntity<UserDTO> response = userService.getUserById(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("user@example.com", response.getBody().getEmail());

        verify(userValidation, times(1)).validateUserById(1);
    }

    @Test
    void putUser_success() {
        PutUserRequest request = new PutUserRequest(
                "NewFirst", "NewLast", "newuser@example.com", "newPassword"
        );

        Users user = new Users();
        user.setEmail("olduser@example.com");

        when(userValidation.validateUserById(1)).thenReturn(user);
        when(passwordEncryptionService.encrypt("newPassword")).thenReturn("encryptedPassword");

        ResponseEntity<UserDTO> response = userService.putUser(request, 1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("newuser@example.com", response.getBody().getEmail());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_success() {
        Users user = new Users();
        user.setActive(true);

        when(userValidation.validateUserById(1)).thenReturn(user);

        ResponseEntity<Void> response = userService.deleteUser(1);

        assertEquals(204, response.getStatusCode().value());
        assertFalse(user.isActive());

        verify(userRepository, times(1)).save(user);
    }
}

