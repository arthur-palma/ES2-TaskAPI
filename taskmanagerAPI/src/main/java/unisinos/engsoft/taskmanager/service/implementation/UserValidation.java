package unisinos.engsoft.taskmanager.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.UserRepository;
import unisinos.engsoft.taskmanager.service.interfaces.IUserValidation;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class UserValidation implements IUserValidation {

    final UserRepository userRepository;


    @Override
    public Users validateUserById(int id) {

        Optional<Users> optUser =  userRepository.findById(id);

        validateUserExists(optUser);

        Users user = optUser.get();

        validateUserActive(user);

        return user;
    }

    @Override
    public Users validateUserByEmail(String email) {

        Optional<Users> optUser =  userRepository.findByEmail(email);

        validateUserExists(optUser);

        Users user = optUser.get();

        validateUserActive(user);

        return user;
    }

    private void validateUserActive(Users user) {
        if(!user.isActive()){
            throw new ResponseStatusException(NOT_FOUND, "User not found");
        }
    }

    private void validateUserExists(Optional<Users> optUser) {
        if(optUser.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "User not found");
        }
    }


}
