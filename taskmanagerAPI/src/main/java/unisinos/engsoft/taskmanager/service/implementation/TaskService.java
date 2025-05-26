package unisinos.engsoft.taskmanager.service.implementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.DTO.CreateTaskRequest;
import unisinos.engsoft.taskmanager.DTO.TaskDTO;
import unisinos.engsoft.taskmanager.mapper.TaskMapper;
import unisinos.engsoft.taskmanager.model.Task;
import unisinos.engsoft.taskmanager.repository.TaskRepository;
import unisinos.engsoft.taskmanager.service.interfaces.ITaskService;
import unisinos.engsoft.taskmanager.service.interfaces.IUserValidation;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static unisinos.engsoft.taskmanager.mapper.TaskMapper.toDTO;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final IUserValidation userValidation;

    @Override
    public ResponseEntity<TaskDTO> createTask(CreateTaskRequest request) {
        var user = userValidation.validateUserById(request.getAssignedTo());

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setUser(user);

        Task savedTask = taskRepository.save(task);


        return ResponseEntity.ok(toDTO(savedTask));
    }

    @Override
    public ResponseEntity<TaskDTO> getTaskById(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Task not found"));

        return ResponseEntity.ok(toDTO(task));
    }

    @Override
    public ResponseEntity<List<TaskDTO>> getTaskByUserId(Integer assignedTo) {
        List<Task> tasks;
        if (assignedTo != null) {
            tasks = taskRepository.findByUser_Id(assignedTo);
        } else {
            tasks = taskRepository.findAll();
        }

        List<TaskDTO> response = tasks.stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<TaskDTO> updateTask(int id, CreateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Task not found"));

        var user = userValidation.validateUserById(request.getAssignedTo());

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setUser(user);

        Task updatedTask = taskRepository.save(task);

        return ResponseEntity.ok(toDTO(updatedTask));
    }

    @Override
    public ResponseEntity<Void> deleteTask(int id) {
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND,"Task not found");
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
