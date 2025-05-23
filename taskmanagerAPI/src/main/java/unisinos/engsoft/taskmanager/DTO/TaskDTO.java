package unisinos.engsoft.taskmanager.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TaskDTO {

    private int id;
    private String title;
    private String description;
    private String status;
    private int assignedTo;

}
