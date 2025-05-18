package unisinos.engsoft.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unisinos.engsoft.taskmanager.model.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {

}