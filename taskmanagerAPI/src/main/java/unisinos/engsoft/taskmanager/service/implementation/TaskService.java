package unisinos.engsoft.taskmanager.service.implementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unisinos.engsoft.taskmanager.DTO.CreateTaskRequest;
import unisinos.engsoft.taskmanager.DTO.TaskDTO;
import unisinos.engsoft.taskmanager.model.Task;
import unisinos.engsoft.taskmanager.repository.TaskRepository;
import unisinos.engsoft.taskmanager.repository.UserRepository;
import unisinos.engsoft.taskmanager.service.interfaces.ITaskService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<TaskDTO> createTask(CreateTaskRequest request) {
        var user = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        TaskDTO response = new TaskDTO(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getUser().getId()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TaskDTO> getTaskByUserId(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task não encontrada"));

        TaskDTO response = new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getUser().getId()
        );

        return ResponseEntity.ok(response);
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
                .map(t -> new TaskDTO(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getStatus(),
                        t.getUser().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<TaskDTO> updateTask(int id, CreateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task não encontrada"));

        var user = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setUser(user);

        Task updatedTask = taskRepository.save(task);

        TaskDTO response = new TaskDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getStatus(),
                updatedTask.getUser().getId()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteTask(int id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task não encontrada");
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
