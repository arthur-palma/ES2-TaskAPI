package unisinos.engsoft.taskmanager.service.implementation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.DTO.LoginRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.config.JwtUtil;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;
import unisinos.engsoft.taskmanager.service.interfaces.IPasswordEncryptionService;
import unisinos.engsoft.taskmanager.service.interfaces.IUserValidation;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private UserRepository userRepository;
    private IPasswordEncryptionService passwordEncryptionService;
    private JwtUtil jwtUtil;
    private IUserValidation userValidation;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncryptionService = mock(IPasswordEncryptionService.class);
        jwtUtil = mock(JwtUtil.class);
        userValidation = mock(IUserValidation.class);
        authService = new AuthService(userRepository, passwordEncryptionService, jwtUtil, userValidation);
    }

    @Test
    void login_success() {
        Users user = new Users();
        user.setEmail("user@example.com");
        user.setPassword("encryptedPassword");

        LoginRequest loginRequest = new LoginRequest("user@example.com", "password");

        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userValidation.validateUserByEmail("user@example.com")).thenReturn(user);
        when(passwordEncryptionService.matches("password", "encryptedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("user@example.com")).thenReturn("fake-jwt-token");

        ResponseEntity<UserDTO> result = authService.login(loginRequest, response);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("user@example.com", result.getBody().getEmail());
        assertEquals("Bearer fake-jwt-token", result.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        Cookie cookie = cookieCaptor.getValue();

        assertEquals("jwt", cookie.getName());
        assertEquals("fake-jwt-token", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }

    @Test
    void login_invalidCredentials_throwsUnauthorized() {
        Users user = new Users();
        user.setEmail("user@example.com");
        user.setPassword("encryptedPassword");

        LoginRequest loginRequest = new LoginRequest("user@example.com", "wrongPassword");

        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userValidation.validateUserByEmail("user@example.com")).thenReturn(user);
        when(passwordEncryptionService.matches("wrongPassword", "encryptedPassword")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authService.login(loginRequest, response));

        assertEquals("401 UNAUTHORIZED \"Invalid credentials\"", exception.getMessage());
    }

    @Test
    void getAuthenticatedUsername_success() {
        UserDetails userDetails = User.withUsername("user@example.com").password("password").roles("USER").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        String username = authService.getAuthenticatedUsername();
        assertEquals("user@example.com", username);
    }

    @Test
    void getAuthenticatedUsername_nullAuthentication_returnsNull() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);

        SecurityContextHolder.setContext(securityContext);

        String username = authService.getAuthenticatedUsername();
        assertNull(username);
    }

    @Test
    void getAuthenticatedUser_success() {
        UserDetails userDetails = User.withUsername("user@example.com").password("password").roles("USER").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Users user = new Users();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Users result = authService.getAuthenticatedUser();

        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void getAuthenticatedUser_notAuthenticated_throwsException() {
        SecurityContextHolder.clearContext();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.getAuthenticatedUser());

        assertEquals("User not authenticated", exception.getMessage());
    }

    @Test
    void getAuthenticatedUser_userNotFound_throwsException() {
        UserDetails userDetails = User.withUsername("user@example.com").password("password").roles("USER").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.getAuthenticatedUser());

        assertEquals("User not found", exception.getMessage());
    }
}
