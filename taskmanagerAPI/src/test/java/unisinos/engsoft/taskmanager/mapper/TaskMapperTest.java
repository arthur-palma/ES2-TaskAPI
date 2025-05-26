package unisinos.engsoft.taskmanager.mapper;

import org.junit.jupiter.api.Test;
import unisinos.engsoft.taskmanager.DTO.TaskDTO;
import unisinos.engsoft.taskmanager.model.Task;
import unisinos.engsoft.taskmanager.model.Users;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    @Test
    void toDTO_whenTaskIsNull_returnsNull() {
        TaskDTO dto = TaskMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void toDTO_whenTaskIsValid_mapsCorrectly() {
        Users user = new Users();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setActive(true);

        Task task = new Task();
        task.setId(10);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("In Progress");
        task.setUser(user);

        TaskDTO dto = TaskMapper.toDTO(task);

        assertNotNull(dto);
        assertEquals(10, dto.getId());
        assertEquals("Test Task", dto.getTitle());
        assertEquals("Test Description", dto.getDescription());
        assertEquals("In Progress", dto.getStatus());

        assertNotNull(dto.getAssignedTo());
        assertEquals(1, dto.getAssignedTo().getId());
        assertEquals("John", dto.getAssignedTo().getFirstName());
        assertEquals("Doe", dto.getAssignedTo().getLastName());
        assertEquals("john.doe@example.com", dto.getAssignedTo().getEmail());
    }
}
