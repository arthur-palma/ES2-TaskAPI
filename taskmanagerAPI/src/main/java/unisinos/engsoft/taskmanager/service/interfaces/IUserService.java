package unisinos.engsoft.taskmanager.service.interfaces;

import org.springframework.http.ResponseEntity;
import unisinos.engsoft.taskmanager.DTO.CreateUserRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;

public interface IUserService {
    ResponseEntity<UserDTO> createUser(CreateUserRequest request);

    ResponseEntity<UserDTO> getUser(int id);

}
