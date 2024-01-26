package hu.webuni.student.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Audited
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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

    /*
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Student student;

     */

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;


    public abstract UserType getUserType();

}
