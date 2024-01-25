package hu.webuni.student.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Audited
@AllArgsConstructor
@NoArgsConstructor
public abstract class AppUser {


    public enum UserType {
        TEACHER, STUDENT;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

    private String name;

    private LocalDate birthdate;

    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;


    public abstract UserType getUserType();

}
