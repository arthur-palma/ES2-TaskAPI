package unisinos.engsoft.taskmanager.service.implementation;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unisinos.engsoft.taskmanager.DTO.CreateUserRequest;
import unisinos.engsoft.taskmanager.DTO.PutUserRequest;
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
  public ResponseEntity<UserDTO> getUser(int id) {
    return null;
  }

  @Override
  public ResponseEntity<UserDTO> putUser(PutUserRequest request, int id) {
    Optional<Users> verUser = userRepository.findById(id);

    if (verUser.isPresent()) {
        Users user = verUser.get();

      user.setEmail(request.getEmail());
      user.setFirstName(request.getFirstName());
      user.setLastName(request.getLastName());
      user.setPassword(
        passwordEncryptionService.encrypt(request.getPassword())
      );

      userRepository.save(user);

      UserDTO userDTO = new UserDTO(
        user.getId(),
        user.getFirstName() + " " + user.getLastName()
      );
      return ResponseEntity.ok(userDTO);
    }
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteUser(int id) {
    Optional<Users> user = userRepository.findById(id);
    if (user.isPresent()) {
      userRepository.deleteById(id);
    }

    return ResponseEntity.noContent().build();
  }
}
