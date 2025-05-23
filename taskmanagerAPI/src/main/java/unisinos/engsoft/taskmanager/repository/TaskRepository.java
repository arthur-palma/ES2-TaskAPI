package unisinos.engsoft.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unisinos.engsoft.taskmanager.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUser_Id(Integer userId);
}
