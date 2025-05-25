package unisinos.engsoft.taskmanager.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class UserValidationTest {

    private UserRepository userRepository;
    private UserValidation userValidation;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userValidation = new UserValidation(userRepository);
    }

    @Test
    void validateUserById_userFoundAndActive_returnsUser() {
        Users user = new Users();
        user.setActive(true);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        Users result = userValidation.validateUserById(1);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void validateUserById_userNotFound_throwsException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userValidation.validateUserById(1));

        assertEquals("404 NOT_FOUND \"User not found\"", ex.getMessage());
    }

    @Test
    void validateUserById_userInactive_throwsException() {
        Users user = new Users();
        user.setActive(false);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userValidation.validateUserById(1));

        assertEquals("404 NOT_FOUND \"User not found\"", ex.getMessage());
    }

    @Test
    void validateUserByEmail_userFoundAndActive_returnsUser() {
        Users user = new Users();
        user.setActive(true);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Users result = userValidation.validateUserByEmail("test@example.com");

        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void validateUserByEmail_userNotFound_throwsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userValidation.validateUserByEmail("test@example.com"));

        assertEquals("404 NOT_FOUND \"User not found\"", ex.getMessage());
    }

    @Test
    void validateUserByEmail_userInactive_throwsException() {
        Users user = new Users();
        user.setActive(false);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userValidation.validateUserByEmail("test@example.com"));

        assertEquals("404 NOT_FOUND \"User not found\"", ex.getMessage());
    }

}
