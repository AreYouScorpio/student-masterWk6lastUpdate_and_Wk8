package hu.webuni.student.service;

import hu.webuni.student.model.Course;
import hu.webuni.student.model.Student;
import hu.webuni.student.model.Teacher;
import hu.webuni.student.repository.CourseRepository;
import hu.webuni.student.repository.StudentRepository;
import hu.webuni.student.repository.TeacherRepository;
import hu.webuni.student.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;


    @Transactional
    public void addInitData() {

        /*
        Student student1 = userRepository.save(new Student("student1", LocalDate.of(1977, 6, 27), 1));
        Student student2 = userRepository.save(new Student("student2", LocalDate.of(1977, 7, 27), 2));
        Student student3 = userRepository.save(new Student("student3", LocalDate.of(1977, 8, 27), 3));
        Student student4 = userRepository.save(new Student("student4", LocalDate.of(1977, 9, 27), 4));


         */


        Student student1 = saveNewStudent("student1", LocalDate.of(1977, 6, 27),1,101,"a", "pass");
        Student student2 = saveNewStudent("student2", LocalDate.of(1977, 7, 27),1,102,"b", "pass");
        Student student3 = saveNewStudent("student3", LocalDate.of(1977, 8, 27),1,103,"c", "pass");
        Student student4 = saveNewStudent("student4", LocalDate.of(1977, 9, 27),1,104,"d", "pass");



        /* old
        Teacher teacher1 = userRepository.save(new Teacher("teacher1", LocalDate.of(1948, 10, 22)));
        Teacher teacher2 = userRepository.save(new Teacher("teacher2", LocalDate.of(1948, 10, 23)));
        Teacher teacher3 = userRepository.save(new Teacher("teacher3", LocalDate.of(1948, 10, 24)));
        Teacher teacher4 = userRepository.save(new Teacher("teacher4", LocalDate.of(1948, 10, 25)));


         */
        // with @SuperBuilder:

        Teacher teacher1 = saveNewTeacher("teacher1", LocalDate.of(1948, 10, 22), "x", "pass");
        Teacher teacher2 = saveNewTeacher("teacher2", LocalDate.of(1948, 10, 23), "y", "pass");
        Teacher teacher3 = saveNewTeacher("teacher3", LocalDate.of(1948, 10, 24), "v", "pass");
        Teacher teacher4 = saveNewTeacher("teacher4", LocalDate.of(1948, 10, 25), "z", "pass");

        Course course1 = courseRepository.save(new Course("angol", Arrays.asList(student1), Set.of(teacher1)));
        Course course2 = courseRepository.save(new Course("nemet", Arrays.asList(student2), Set.of(teacher2)));
        Course course3 = courseRepository.save(new Course("holland", Arrays.asList(student3), Set.of(teacher3)));
        Course course4 = courseRepository.save(new Course("magyar", Arrays.asList(student4, student2), Set.of(teacher4)));

        /*
        student1.setCentralId(101);
        student2.setCentralId(102);
        student3.setCentralId(103);
        student4.setCentralId(104);

         */

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
        userRepository.deleteAll();
    }

    @Transactional
    public void deleteAudTables() {
        jdbcTemplate.update("DELETE FROM course_aud");
        jdbcTemplate.update("DELETE FROM student_aud");
        jdbcTemplate.update("DELETE FROM teacher_aud");
        jdbcTemplate.update("DELETE FROM app_user_aud");
        jdbcTemplate.update("DELETE FROM app_user_roles_aud");
    }


    //mukodik, de nem enged be:
    /*
    @Transactional
    public void createUsersIfNeeded() {
        if (!userRepository.existsById("admin")) {
            userRepository.save(new AppUser("admin", passwordEncoder.encode("pass"),
                    Set.of(new SimpleGrantedAuthority("admin"), new SimpleGrantedAuthority("user"))));
        }
        if (!userRepository.existsById("user")) {
            userRepository.save(new AppUser("user", passwordEncoder.encode("pass"),
                    Set.of(new SimpleGrantedAuthority("user"))));
        }
        if (!userRepository.existsById("akos")) {
            userRepository.save(new AppUser("akos", passwordEncoder.encode("Almira123"),
                    Set.of(new SimpleGrantedAuthority("admin"), new SimpleGrantedAuthority("user"))));
        }
    }

     */

    //nem mukodik, de regen ezzel mukodott:

    @Transactional
    public void createUsersIfNeeded() {

        /* old

        if(!userRepository.existsById("admin")) {
            userRepository.save(
                    new AppUser("admin", passwordEncoder.encode("pass"),
                            Set.of("admin", "user") ));}
        if(!userRepository.existsById("user")) {
            userRepository.save(
                    new AppUser("user",
                            passwordEncoder.encode("pass"),
                            Set.of("user") ));}
        if(!userRepository.existsById("akos")) {
            userRepository.save(
                    new AppUser("akos",
                            passwordEncoder.encode("Almira123"),
                            Set.of("admin", "user") ));}


    }

         */

        //new:
        if (!userRepository.existsByUsername("admin")) {
            System.out.println("InitDb - admin username not existing, we create one");

            /*
            Teacher adminTeacher = new Teacher("admin", LocalDate.of(1990, 1, 1));
            adminTeacher.setPassword(passwordEncoder.encode("pass"));
            adminTeacher.setRoles(Set.of("admin", "user"));
            adminTeacher.setName("admin");
            //teacherRepository.save(adminTeacher);
            teacherRepository.save(adminTeacher);
             */
            Teacher adminTeacher = saveNewTeacher("MissingTeacher", LocalDate.of(2000, 10, 10), "admin", "pass");
            adminTeacher.setRoles(Set.of("admin", "user"));
        }
        if (!userRepository.existsByUsername("user")) {
            System.out.println("InitDb - user username not existing, we create one");
            /*
            Student userStudent = new Student("user", LocalDate.of(1995, 1, 1), 1);
            userStudent.setPassword(passwordEncoder.encode("pass"));
            userStudent.setRoles(Set.of("user"));
            userStudent.setName("user");

            //studentRepository.save(userStudent);
            studentRepository.save(userStudent);

             */
            Student userStudent = saveNewStudent("MissingStudent", LocalDate.of(2000, 10, 10), 1, 111, "user", "pass");
            userStudent.setRoles(Set.of("user"));
        }
        if (!userRepository.existsByUsername("akos")) {
            System.out.println("InitDb - akos username not existing, we create one");
            /*
            Teacher akosTeacher = new Teacher("akos", LocalDate.of(1985, 1, 1));
            akosTeacher.setPassword(passwordEncoder.encode("Almira123"));
            akosTeacher.setRoles(Set.of("admin", "user"));
            akosTeacher.setName("akos");

            //teacherRepository.save(akosTeacher);
            teacherRepository.save(akosTeacher);

             */
            Teacher akosTeacher = saveNewTeacher("Akos", LocalDate.of(1977, 6, 27), "akos", "Almira123");
            akosTeacher.setRoles(Set.of("admin", "user"));

        }

    }

    private Student saveNewStudent(String name, LocalDate birthdate, int semester, int centralId, String username, String pass) {
        return studentRepository.save(
                Student.builder()
                        .name(name)
                        .birthdate(birthdate)
                        .semester(semester)
                        .centralId(centralId)
                        .username(username)
                        .password(passwordEncoder.encode(pass))
                        .build());
    }

    private Teacher saveNewTeacher(String name, LocalDate birthdate, String username, String pass) {
        return teacherRepository.save(
                Teacher.builder()
                        .name(name)
                        .birthdate(birthdate)
                        .username(username)
                        .password(passwordEncoder.encode(pass))
                        .build());
    }


}


