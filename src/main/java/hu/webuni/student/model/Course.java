package hu.webuni.student.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true) -> in @Data included with this setup
//@ToString(onlyExplicitlyIncluded = true) -> in @Data included with this setup (also @Getter, @Setter, @Req.Constr)
@Entity
@Audited
@NamedEntityGraph(
        name = "Course.students",
        attributeNodes = @NamedAttributeNode("students"))
@NamedEntityGraph(
        name = "Course.teachers",
        attributeNodes = @NamedAttributeNode("teachers"))
//@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue // (strategy = GenerationType.AUTO)
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

/*
    @Column(name = "year", nullable = false, insertable = false, updatable = false) // This modification indicates that this property is not responsible for inserting and updating the "year" column in the database.
    //@Column(name = "year", columnDefinition = "integer default 2023", nullable = false, insertable = false, updatable = false) // This modification indicates that this property is not responsible for inserting and updating the "year" column in the database.
    private Integer year = 2023;


 */




    //@Size(min = 3, max = 20)
    @ToString.Include
    @NotNull
    private String name;



    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Student> students;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Teacher> teachers;


    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    private Set<TimeTableItem> timeTableItems;

    //@JoinColumn(name = "semester_id")  // Specify the mapping for the foreign key
    private Semester semester;

    public void addTimeTableItem(TimeTableItem timeTableItem) {
        timeTableItem.setCourse(this);
        if(this.timeTableItems == null)
            this.timeTableItems = new HashSet<>();
        this.timeTableItems.add(timeTableItem);
    }


//    public Course(String name, Student student, Teacher teacher) {
//        this.name = name;
//        this.student = student;
//        this.teacher = teacher;
//    }


    public Course( int year, String name,  List<Student> students, Set<Teacher> teachers, Semester semester) {

      //  this.year = year;
        this.name = name;
        this.students = students;
        this.teachers = teachers;
        this.semester = semester;
    }
/*
    // Overloaded constructor with a default year value
    public Course(String name, List<Student> students, Set<Teacher> teachers) {
        this(2023, name, students, teachers, Semester.fromMidSemesterDay()); // Default year is 2023
    }


 */




}
