package unisinos.engsoft.taskmanager.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptionServiceTest {

    private PasswordEncryptionService passwordEncryptionService;

    @BeforeEach
    void setUp() {
        passwordEncryptionService = new PasswordEncryptionService();
    }

    @Test
    void encrypt_shouldGenerateHashedPassword() {
        String rawPassword = "senhaSegura123";
        String hashedPassword = passwordEncryptionService.encrypt(rawPassword);

        assertNotNull(hashedPassword);
        assertNotEquals(rawPassword, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$"));
    }

    @Test
    void matches_shouldReturnTrueForMatchingPassword() {
        String rawPassword = "senhaSegura123";
        String hashedPassword = passwordEncryptionService.encrypt(rawPassword);

        boolean result = passwordEncryptionService.matches(rawPassword, hashedPassword);

        assertTrue(result);
    }

    @Test
    void matches_shouldReturnFalseForNonMatchingPassword() {
        String rawPassword = "senhaSegura123";
        String hashedPassword = passwordEncryptionService.encrypt(rawPassword);

        boolean result = passwordEncryptionService.matches("senhaErrada", hashedPassword);

        assertFalse(result);
    }
}