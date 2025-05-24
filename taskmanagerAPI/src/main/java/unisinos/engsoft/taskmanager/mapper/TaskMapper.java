package unisinos.engsoft.taskmanager.mapper;

import unisinos.engsoft.taskmanager.DTO.TaskDTO;
import unisinos.engsoft.taskmanager.model.Task;

public class TaskMapper {

    public static TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }

        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .assignedTo(UserMapper.toDTO(task.getUser()))
                .build();
    }

}
