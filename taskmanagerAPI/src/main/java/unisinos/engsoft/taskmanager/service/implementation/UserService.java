package unisinos.engsoft.taskmanager.service.implementation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.DTO.CreateUserRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;
import unisinos.engsoft.taskmanager.service.interfaces.IPasswordEncryptionService;
import unisinos.engsoft.taskmanager.service.interfaces.IUserService;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final IPasswordEncryptionService passwordEncryptionService;


    @Override
    public ResponseEntity<UserDTO> createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Users user =  new Users();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncryptionService.encrypt(request.getPassword()));


        userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());


        return ResponseEntity.ok(userDTO);
    }

    @Override
    public ResponseEntity<UserDTO> getUser(int id) {
        return null;
    }
}
