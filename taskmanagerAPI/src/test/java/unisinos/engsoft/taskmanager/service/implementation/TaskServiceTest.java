package unisinos.engsoft.taskmanager.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import unisinos.engsoft.taskmanager.DTO.CreateTaskRequest;
import unisinos.engsoft.taskmanager.DTO.TaskDTO;
import unisinos.engsoft.taskmanager.model.Task;
import unisinos.engsoft.taskmanager.model.Users;
import unisinos.engsoft.taskmanager.repository.TaskRepository;
import unisinos.engsoft.taskmanager.service.interfaces.IUserValidation;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private IUserValidation userValidation;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Users user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new Users();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        task = new Task();
        task.setId(100);
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setStatus("Pending");
        task.setUser(user);
    }

    @Test
    void createTask_success() {
        CreateTaskRequest request = new CreateTaskRequest("Test Task", "Description", "Pending", 1);

        when(userValidation.validateUserById(1)).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        ResponseEntity<TaskDTO> response = taskService.createTask(request);

        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTaskById_whenExists_returnsTask() {
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));

        ResponseEntity<TaskDTO> response = taskService.getTaskById(100);

        assertNotNull(response.getBody());
        assertEquals(100, response.getBody().getId());
    }

    @Test
    void getTaskById_whenNotExists_throwsException() {
        when(taskRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.getTaskById(999));
    }

    @Test
    void getTaskByUserId_withAssignedTo_returnsTasks() {
        when(taskRepository.findByUser_Id(1)).thenReturn(List.of(task));

        ResponseEntity<List<TaskDTO>> response = taskService.getTaskByUserId(1);

        assertEquals(1, response.getBody().size());
        assertEquals(100, response.getBody().get(0).getId());
    }

    @Test
    void getTaskByUserId_withNull_returnsAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        ResponseEntity<List<TaskDTO>> response = taskService.getTaskByUserId(null);

        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateTask_whenExists_updatesSuccessfully() {
        CreateTaskRequest request = new CreateTaskRequest("Updated Task", "Updated Desc", "Completed", 1);

        when(taskRepository.findById(100)).thenReturn(Optional.of(task));
        when(userValidation.validateUserById(1)).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        ResponseEntity<TaskDTO> response = taskService.updateTask(100, request);

        assertNotNull(response.getBody());
        assertEquals("Updated Task", response.getBody().getTitle());
    }

    @Test
    void updateTask_whenNotExists_throwsException() {
        CreateTaskRequest request = new CreateTaskRequest("Updated Task", "Updated Desc", "Completed", 1);

        when(taskRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.updateTask(999, request));
    }

    @Test
    void deleteTask_whenExists_deletesSuccessfully() {
        when(taskRepository.existsById(100)).thenReturn(true);

        ResponseEntity<Void> response = taskService.deleteTask(100);

        assertEquals(204, response.getStatusCode().value());
        verify(taskRepository, times(1)).deleteById(100);
    }

    @Test
    void deleteTask_whenNotExists_throwsException() {
        when(taskRepository.existsById(999)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> taskService.deleteTask(999));
    }
}