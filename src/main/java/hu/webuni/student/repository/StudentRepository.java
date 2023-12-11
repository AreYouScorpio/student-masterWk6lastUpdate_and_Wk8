package hu.webuni.student.repository;

import hu.webuni.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StudentRepository extends
        JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student>,
        QuerydslPredicateExecutor<Student> {


    //QueryDSL 2. course-hoz ezt is hozzá kell adni, h Predicate is átadható legyen, az AirportService-ben (korábbi FlightSpecben )

}
