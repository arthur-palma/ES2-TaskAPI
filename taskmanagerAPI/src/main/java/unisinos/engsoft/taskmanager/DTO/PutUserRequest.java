package unisinos.engsoft.taskmanager.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PutUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
