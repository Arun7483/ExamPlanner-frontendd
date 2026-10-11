package com.cts.Backend_ExamPlanner.repositories;
import com.cts.Backend_ExamPlanner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}