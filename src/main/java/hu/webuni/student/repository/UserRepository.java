package hu.webuni.student.repository;

import hu.webuni.student.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {


    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByFacebookId(String facebookId);
    Optional<AppUser> findByGoogleId(String googleId);


    boolean existsByUsername(String username);
}
