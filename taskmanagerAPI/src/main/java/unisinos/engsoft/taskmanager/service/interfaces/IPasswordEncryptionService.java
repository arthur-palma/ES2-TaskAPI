package unisinos.engsoft.taskmanager.service.interfaces;

public interface IPasswordEncryptionService {
    public String encrypt(String password);

    public boolean matches(String rawPassword, String hashedPassword);
}
