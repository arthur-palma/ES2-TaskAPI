package unisinos.engsoft.taskmanager.mapper;

import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.model.Users;

public class UserMapper {

     public static UserDTO toDTO(Users user) {
          if (user == null) {
               return null;
          }

          return UserDTO.builder()
                  .id(user.getId())
                  .firstName(user.getFirstName())
                  .lastName(user.getLastName())
                  .email(user.getEmail())
                  .build();
     }
}
