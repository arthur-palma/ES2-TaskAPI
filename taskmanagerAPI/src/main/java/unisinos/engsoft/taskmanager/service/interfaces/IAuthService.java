package unisinos.engsoft.taskmanager.service.interfaces;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import unisinos.engsoft.taskmanager.DTO.LoginRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.model.Users;

public interface IAuthService {

    ResponseEntity<UserDTO> login(LoginRequest loginRequest, HttpServletResponse response);

    Users getAuthenticatedUser();
}
