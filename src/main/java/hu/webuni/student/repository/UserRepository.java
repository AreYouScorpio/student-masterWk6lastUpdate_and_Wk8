package hu.webuni.student.repository;

import hu.webuni.student.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, String> {

    boolean existsById(String username);
}
