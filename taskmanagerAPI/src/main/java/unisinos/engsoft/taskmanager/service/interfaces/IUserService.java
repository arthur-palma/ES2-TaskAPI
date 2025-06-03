package unisinos.engsoft.taskmanager.service.interfaces;

import org.springframework.http.ResponseEntity;
import unisinos.engsoft.taskmanager.DTO.CreateUserRequest;
import unisinos.engsoft.taskmanager.DTO.PutUserRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.model.Users;

import java.util.List;

public interface IUserService {
    ResponseEntity<UserDTO> createUser(CreateUserRequest request);

    ResponseEntity<UserDTO> getUserById(int id);

    ResponseEntity<UserDTO> putUser(PutUserRequest request, int id);

    ResponseEntity<Void> deleteUser(int id);

}
