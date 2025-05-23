package unisinos.engsoft.taskmanager.service.interfaces;

import org.springframework.http.ResponseEntity;
import unisinos.engsoft.taskmanager.DTO.TaskDTO;
import unisinos.engsoft.taskmanager.DTO.CreateTaskRequest;
import java.util.List;

public interface ITaskService {

    ResponseEntity<TaskDTO> createTask(CreateTaskRequest request);

    ResponseEntity<TaskDTO> updateTask(int id, CreateTaskRequest request);

    ResponseEntity<Void> deleteTask(int id);

    ResponseEntity<TaskDTO> getTaskByUserId(int id);

    ResponseEntity<List<TaskDTO>> getTaskByUserId(Integer assignedTo);
}