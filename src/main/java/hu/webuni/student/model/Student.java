package hu.webuni.student.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString//(onlyExplicitlyIncluded = true)
@Entity
//@Cacheable
@Audited
//@Getter
//@Setter
public class Student extends AppUser{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /*
    @ToString.Include
    //@Size(min = 3, max = 20)
    private String name;
    //@Size (min = 3, max = 10)
    private LocalDate birthdate;


     */
    @ManyToMany(mappedBy = "students")
    private Set<Course> courses;
    private int semester;

    private int centralId;
    private int freeSemester;

    private int cashPaidIn;

    /*
    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;


     */
    @OneToOne
    //@JoinColumn(name = "id") //idegen kulcs a masik oldalon ez legyen
    @JoinColumn(name = "image_id") //idegen kulcs a masik oldalon ez legyen // ne legyen id, m osszeutkozik, mar van egy id-nk
    private Image image;

    private String imageLocation;



/*
    public Student(String name, LocalDate birthdate, int semester) {
        this.name = name;
        this.birthdate = birthdate;
        this.semester = semester;
    }


 */


    @Override
    public UserType getUserType() {
        return UserType.STUDENT;
    }




}
