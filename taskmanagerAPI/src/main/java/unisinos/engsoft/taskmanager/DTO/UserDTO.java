package unisinos.engsoft.taskmanager.DTO;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    public int id;

    public String firstName;

    public String lastName;

    public String email;

}
