package unisinos.engsoft.taskmanager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unisinos.engsoft.taskmanager.DTO.CreateUserRequest;
import unisinos.engsoft.taskmanager.DTO.PutUserRequest;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.service.interfaces.IUserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final IUserService iUserService;

    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request){
        return iUserService.createUser(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id){
        return iUserService.getUser(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> putUser(@RequestBody PutUserRequest request){
        return iUserService.putUser(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
        return iUserService.deleteUser(id);
    }
}
