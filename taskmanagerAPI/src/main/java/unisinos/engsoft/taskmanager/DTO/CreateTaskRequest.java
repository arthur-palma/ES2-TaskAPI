package unisinos.engsoft.taskmanager.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTaskRequest {

    private String  title;
    private String  description;
    private String  status;
    private int assignedTo;
}
