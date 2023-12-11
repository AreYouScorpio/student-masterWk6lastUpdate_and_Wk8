package hu.webuni.student.repository;

import hu.webuni.student.model.Course;
import hu.webuni.student.model.QCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Iterator;
import java.util.Optional;

public interface CourseRepository extends
        JpaRepository<Course, Long>,

        QuerydslPredicateExecutor<Course>,
        QuerydslBinderCustomizer<QCourse>,
        QueryDslWithEntityGraphRepository<Course, Long> {


//    // tul sok talalat lenne, Descartes-szorzat, tul sok eredmenysor DB-bol
//    @EntityGraph(attributePaths = {"teachers", "students"})
//    List<Course> findAllWithTeachersAndStudents(Predicate predicate);
//
//    // nincs Descartes-szorzat, de nem tudja a Spring Data QueryDsl tamogatasa
//    @EntityGraph(attributePaths = {"teachers"})
//    List<Course> findAllWithTeachers(Predicate predicate);
//
//
//    @EntityGraph(attributePaths = {"students"})
//    List<Course> findAllWithStudents(Predicate predicate);


    @Override
    default void customize(QuerydslBindings bindings, QCourse course) {
        bindings.bind(course.name).first((path, value) -> path.startsWithIgnoreCase(value));
        bindings.bind(course.teachers.any().name).first((path, value) -> path.startsWithIgnoreCase(value)); //teacherS es sudentS kapcsolatot jelol, Maven-nel generaltatni a Q-kat hozza
        bindings.bind(course.students.any().semester).all((path, values) -> {
            if(values.size() != 2)
                return Optional.empty();
            Iterator<? extends Integer> iterator = values.iterator();
            Integer from = iterator.next();
            Integer to = iterator.next();

            return Optional.of(path.between(from, to));
        });
    }



    //QueryDSL 2. course-hoz ezt is hozzá kell adni, h Predicate is átadható legyen, az AirportService-ben (korábbi FlightSpecben )

}
