package unisinos.engsoft.taskmanager.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.DTO.CreateUserRequest;
import unisinos.engsoft.taskmanager.DTO.PutUserRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;
import unisinos.engsoft.taskmanager.service.interfaces.IPasswordEncryptionService;
import unisinos.engsoft.taskmanager.service.interfaces.IUserService;
import unisinos.engsoft.taskmanager.service.interfaces.IUserValidation;

import java.util.Optional;

import static unisinos.engsoft.taskmanager.mapper.UserMapper.toDTO;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final IPasswordEncryptionService passwordEncryptionService;

    private final IUserValidation userValidation;

    @Override
    public ResponseEntity<UserDTO> createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already in use");
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncryptionService.encrypt(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(toDTO(user));
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(int id) {
        Users user = userValidation.validateUserById(id);

        return ResponseEntity.ok(toDTO(user));
    }


    @Override
    public ResponseEntity<UserDTO> putUser(PutUserRequest request, int id) {
        Users user = userValidation.validateUserById(id);
        Optional<Users> userByEmail = userRepository.findByEmail(request.getEmail());

        if( userByEmail.isPresent()){
            if(user.getId() != userByEmail.get().getId()){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "email already in use");
            }
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncryptionService.encrypt(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(toDTO(user));
    }

    @Override
    public ResponseEntity<Void> deleteUser(int id){
        Users user = userValidation.validateUserById(id);

        user.setActive(false);
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
