package hu.webuni.student.repository;

import hu.webuni.student.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsById(String username);
}
