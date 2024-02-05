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
//@Inheritance(strategy = InheritanceType.JOINED) //using @Inheritance(strategy = InheritanceType.JOINED) can be a good option, especially if you have a complex class hierarchy and want to avoid issues related to the single table strategy. With InheritanceType.JOINED, each subclass (e.g., Teacher and Student) has its own table, and a join operation is used to retrieve data from the related tables. This strategy tends to be more normalized and avoids having a large table with many null columns.
public abstract class AppUser {


    public enum UserType {
        TEACHER, STUDENT;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

    @ToString.Include
    private String name;

    private LocalDate birthdate;

    private String username;
    private String password;

    /*
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Student student;

     */

    private String facebookId;

    private String googleId;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;


    public abstract UserType getUserType();

}
