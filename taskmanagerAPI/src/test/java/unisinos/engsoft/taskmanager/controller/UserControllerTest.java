package unisinos.engsoft.taskmanager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import unisinos.engsoft.taskmanager.DTO.CreateUserRequest;
import unisinos.engsoft.taskmanager.DTO.PutUserRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.service.interfaces.IUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

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
    void createUser_returnsCreatedUser() {
        CreateUserRequest request = new CreateUserRequest("Test"
                ,"User", "test@example.com","password");
        when(userService.createUser(request)).thenReturn(ResponseEntity.ok(userDTO));

        ResponseEntity<UserDTO> response = userController.createUser(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).createUser(request);
    }

    @Test
    void getUser_returnsUser() {
        when(userService.getUserById(1)).thenReturn(ResponseEntity.ok(userDTO));

        ResponseEntity<UserDTO> response = userController.getUser(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void putUser_returnsUpdatedUser() {
        PutUserRequest request = new PutUserRequest("Updated"
                ,"User", "update@example.com","password");;
        when(userService.putUser(request, 1)).thenReturn(ResponseEntity.ok(userDTO));

        ResponseEntity<UserDTO> response = userController.putUser(request, 1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).putUser(request, 1);
    }

    @Test
    void deleteUser_returnsNoContent() {
        when(userService.deleteUser(1)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = userController.deleteUser(1);

        assertEquals(204, response.getStatusCode().value());
        verify(userService, times(1)).deleteUser(1);
    }
}
