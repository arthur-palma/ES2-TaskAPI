package unisinos.engsoft.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unisinos.engsoft.taskmanager.model.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {


    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

}