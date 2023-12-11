package hu.webuni.student.service;

import hu.webuni.student.model.Course;
import hu.webuni.student.model.Student;
import hu.webuni.student.model.Teacher;
import hu.webuni.student.repository.CourseRepository;
import hu.webuni.student.repository.StudentRepository;
import hu.webuni.student.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class InitDbService {


    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Transactional
    public void addInitData() {
        Student student1 = studentRepository.save(new Student("student1", LocalDate.of(1977, 6, 27), 1));
        Student student2 = studentRepository.save(new Student("student2", LocalDate.of(1977, 7, 27), 2));
        Student student3 = studentRepository.save(new Student("student3", LocalDate.of(1977, 8, 27), 3));
        Student student4 = studentRepository.save(new Student("student4", LocalDate.of(1977, 9, 27), 4));

        Teacher teacher1 = teacherRepository.save(new Teacher("teacher1", LocalDate.of(1948, 10, 22)));
        Teacher teacher2 = teacherRepository.save(new Teacher("teacher2", LocalDate.of(1948, 10, 23)));
        Teacher teacher3 = teacherRepository.save(new Teacher("teacher3", LocalDate.of(1948, 10, 24)));
        Teacher teacher4 = teacherRepository.save(new Teacher("teacher4", LocalDate.of(1948, 10, 25)));


        Course course1 = courseRepository.save(new Course("angol", Set.of(student1), Set.of(teacher1)));
        Course course2 = courseRepository.save(new Course("nemet", Set.of(student2), Set.of(teacher2)));
        Course course3 = courseRepository.save(new Course("holland", Set.of(student3), Set.of(teacher3)));
        Course course4 = courseRepository.save(new Course("magyar", Set.of(student4), Set.of(teacher4)));

        student1.setCentralId(101);
        student2.setCentralId(102);
        student3.setCentralId(103);
        student4.setCentralId(104);

//        Course course1 = courseRepository.save(new Course("angol", student1, teacher1));
//        Course course2 = courseRepository.save(new Course("nemet", student2, teacher2));
//        Course course3 = courseRepository.save(new Course("holland", student3, teacher3));
//        Course course4 = courseRepository.save(new Course("magyar", student4, teacher4));


    }


    @Transactional
    public void deleteDb() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Transactional
    public void deleteAudTables(){
        jdbcTemplate.update("DELETE FROM course_aud");
        jdbcTemplate.update("DELETE FROM student_aud");
        jdbcTemplate.update("DELETE FROM teacher_aud");
    }

}


