package unisinos.engsoft.taskmanager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import unisinos.engsoft.taskmanager.DTO.CreateTaskRequest;
import unisinos.engsoft.taskmanager.DTO.TaskDTO;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.service.interfaces.ITaskService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private ITaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private CreateTaskRequest createTaskRequest;

    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setEmail("test@example.com");

        createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setTitle("Test Task");
        createTaskRequest.setDescription("Test description");
        createTaskRequest.setStatus("OPEN");
        createTaskRequest.setAssignedTo(1);

        taskDTO = new TaskDTO();
        taskDTO.setId(100);
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test description");
        taskDTO.setStatus("OPEN");
        taskDTO.setAssignedTo(userDTO);
    }

    @Test
    void createTask_shouldReturnCreatedTask() {
        when(taskService.createTask(createTaskRequest)).thenReturn(ResponseEntity.ok(taskDTO));

        ResponseEntity<TaskDTO> response = taskController.createTask(createTaskRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(taskDTO, response.getBody());
        verify(taskService, times(1)).createTask(createTaskRequest);
    }

    @Test
    void getTask_shouldReturnTaskById() {
        int taskId = 100;
        when(taskService.getTaskById(taskId)).thenReturn(ResponseEntity.ok(taskDTO));

        ResponseEntity<TaskDTO> response = taskController.getTask(taskId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(taskDTO, response.getBody());
        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void getTaskByUser_shouldReturnTaskList() {
        Integer assignedTo = 1;
        List<TaskDTO> tasks = List.of(taskDTO);
        when(taskService.getTaskByUserId(assignedTo)).thenReturn(ResponseEntity.ok(tasks));

        ResponseEntity<List<TaskDTO>> response = taskController.getTaskByUser(assignedTo);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals(taskDTO, response.getBody().get(0));
        verify(taskService, times(1)).getTaskByUserId(assignedTo);
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() {
        int taskId = 100;
        when(taskService.updateTask(taskId, createTaskRequest)).thenReturn(ResponseEntity.ok(taskDTO));

        ResponseEntity<TaskDTO> response = taskController.updateTask(taskId, createTaskRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(taskDTO, response.getBody());
        verify(taskService, times(1)).updateTask(taskId, createTaskRequest);
    }

    @Test
    void deleteTask_shouldReturnNoContent() {
        int taskId = 100;
        when(taskService.deleteTask(taskId)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        assertEquals(204, response.getStatusCode().value());
        verify(taskService, times(1)).deleteTask(taskId);
    }
}