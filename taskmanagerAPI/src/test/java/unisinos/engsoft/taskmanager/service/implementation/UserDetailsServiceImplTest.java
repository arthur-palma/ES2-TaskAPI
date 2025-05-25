package unisinos.engsoft.taskmanager.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername_success() {
        Users user = new Users();
        user.setEmail("user@example.com");
        user.setPassword("encryptedPassword");
        user.setActive(true);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("user@example.com");

        assertNotNull(userDetails);
        assertEquals("user@example.com", userDetails.getUsername());
        assertEquals("encryptedPassword", userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_userNotFound_throwsUnauthorized() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userDetailsService.loadUserByUsername("user@example.com"));

        assertEquals("401 UNAUTHORIZED \"Token Invalido\"", exception.getMessage());
    }

    @Test
    void loadUserByUsername_userNotActive_throwsUnauthorized() {
        Users user = new Users();
        user.setEmail("user@example.com");
        user.setPassword("encryptedPassword");
        user.setActive(false);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userDetailsService.loadUserByUsername("user@example.com"));

        assertEquals("401 UNAUTHORIZED \"Token Invalido\"", exception.getMessage());
    }
}
