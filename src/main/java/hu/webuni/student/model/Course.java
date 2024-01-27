package hu.webuni.student.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Audited
@NamedEntityGraph(
        name = "Course.students",
        attributeNodes = @NamedAttributeNode("students"))
@NamedEntityGraph(
        name = "Course.teachers",
        attributeNodes = @NamedAttributeNode("teachers"))
public class Course {

    @Id
    @GeneratedValue // (strategy = GenerationType.AUTO)
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

    //@Size(min = 3, max = 20)
    @ToString.Include
    private String name;


    @ManyToMany(fetch = FetchType.EAGER)
    private List<Student> students;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Teacher> teachers;


    @OneToMany(mappedBy = "course")
    private Set<TimeTableItem> timeTableItems;

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


    public Course(String name, List<Student> students, Set<Teacher> teachers) {
        this.name = name;
        this.students = students;
        this.teachers = teachers;
    }

    public Course(String name) {
        this.name = name;
    }

}
