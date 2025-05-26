package unisinos.engsoft.taskmanager.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import unisinos.engsoft.taskmanager.DTO.LoginRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.service.interfaces.IAuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private IAuthService authService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private AuthController authController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setEmail("test@example.com");
    }

    @Test
    void login_whenValidCredentials_returnsUserDTO() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(authService.login(loginRequest, httpServletResponse))
                .thenReturn(ResponseEntity.ok(userDTO));

        ResponseEntity<UserDTO> response = authController.login(loginRequest, httpServletResponse);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(userDTO, response.getBody());

        verify(authService, times(1)).login(loginRequest, httpServletResponse);
    }
}
