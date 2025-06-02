package unisinos.engsoft.taskmanager.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unisinos.engsoft.taskmanager.DTO.CreateTaskRequest;
import unisinos.engsoft.taskmanager.DTO.TaskDTO;
import unisinos.engsoft.taskmanager.service.interfaces.ITaskService;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final ITaskService iTaskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskRequest request)
    {
        return iTaskService.createTask(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable int id)
    {
        return iTaskService.getTaskById(id);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTaskByUser(@RequestParam(required = false) Integer assignedTo)
    {
        return iTaskService.getTaskByUserId(assignedTo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO>updateTask(@PathVariable int id, @RequestBody CreateTaskRequest request)
    {
        return iTaskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteTask(@PathVariable int id)
    {
        return iTaskService.deleteTask(id);

    }

}
