package hu.webuni.student.model;

import lombok.*;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Cacheable
@Audited
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue // (strategy = GenerationType.AUTO)
    @ToString.Include
    @EqualsAndHashCode.Include()
    private long id;

    @ToString.Include
    //@Size(min = 3, max = 20)
    private String name;
    //@Size (min = 3, max = 10)
    private LocalDate birthdate;
    private int semester;

    private int centralId;
    private int freeSemester;

    @OneToOne
    @JoinColumn(name = "id") //idegen kulcs a masik oldalon ez legyen
    private Image image;

    private String imageLocation;

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses;



    public Student(String name, LocalDate birthdate, int semester) {
        this.name = name;
        this.birthdate = birthdate;
        this.semester = semester;
    }


}
