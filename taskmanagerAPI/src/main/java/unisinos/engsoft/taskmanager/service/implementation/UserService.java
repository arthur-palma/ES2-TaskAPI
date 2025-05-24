package unisinos.engsoft.taskmanager.service.implementation;

import java.util.Optional;
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

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static unisinos.engsoft.taskmanager.mapper.UserMapper.toDTO;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

  private final UserRepository userRepository;

  private final IPasswordEncryptionService passwordEncryptionService;

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

    UserDTO userDTO = new UserDTO(
      user.getId(),
      user.getFirstName() + " " + user.getLastName()
    );

    return ResponseEntity.ok(userDTO);
  }

  @Override
  public ResponseEntity<UserDTO> getUserById(int id) {

    Optional<Users> optUser = userRepository.getUsersById(id);

    if (optUser.isEmpty()) {
      throw new ResponseStatusException(NOT_FOUND, "User not found");
    }

    return ResponseEntity.ok(toDTO(optUser.get()));
  }


  @Override
  public ResponseEntity<UserDTO> putUser(PutUserRequest request, int id) {
    Optional<Users> verUser = userRepository.findById(id);

    if (verUser.isEmpty()) {
      throw new ResponseStatusException(NOT_FOUND, "User not found");
    }

      Users user = verUser.get();

      user.setEmail(request.getEmail());
      user.setFirstName(request.getFirstName());
      user.setLastName(request.getLastName());
      user.setPassword(
        passwordEncryptionService.encrypt(request.getPassword())
      );

      userRepository.save(user);

      return ResponseEntity.ok(toDTO(user));
  }

  @Override
  public ResponseEntity<Void> deleteUser(int id) {
    Optional<Users> userOpt = userRepository.findById(id);

    if (userOpt.isEmpty()) {
      throw new ResponseStatusException(NOT_FOUND, "User not found");
    }

    Users user = userOpt.get();
    user.setActive(false);
    userRepository.save(user);

    return ResponseEntity.noContent().build();
  }
}
