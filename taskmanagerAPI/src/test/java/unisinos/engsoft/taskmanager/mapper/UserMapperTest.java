package unisinos.engsoft.taskmanager.mapper;

import org.junit.jupiter.api.Test;
import unisinos.engsoft.taskmanager.DTO.UserDTO;
import unisinos.engsoft.taskmanager.model.Users;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDTO_whenUserIsNull_returnsNull() {
        UserDTO dto = UserMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void toDTO_whenUserIsValid_mapsCorrectly() {
        Users user = new Users();
        user.setId(1);
        user.setFirstName("Alice");
        user.setLastName("Smith");
        user.setEmail("alice.smith@example.com");
        user.setPassword("senha123");
        user.setActive(true);

        UserDTO dto = UserMapper.toDTO(user);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Alice", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("alice.smith@example.com", dto.getEmail());
    }
}
