package hu.webuni.student.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import hu.webuni.student.model.QTeacher;
import hu.webuni.student.model.Teacher;
import hu.webuni.student.repository.CourseRepository;
import hu.webuni.student.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;

    @Transactional
    public Teacher save(Teacher teacher) {
        //a takeoff/landing airportból csak az id-t vesszük figyelembe, már létezniük kell
//        student.setTakeoff(studentRepository.findById(flight.getTakeoff().getId()).get());
//        student.setLanding(studentRepository.findById(flight.getLanding().getId()).get());
        return teacherRepository.save(teacher);
    }

    public List<Teacher> findTeachersByExample(Teacher example) {
        long id = example.getId();
        String name = example.getName();

        LocalDate birthdate = example.getBirthdate();


        //Specification<Flight> spec = Specification.where(null); // üres Specification, ami semmire nem szűr

        // a FlightSpecifications feltételeit ide írjuk bele közvetlen --->

        ArrayList<Predicate> predicates = new ArrayList<Predicate>();

        QTeacher teacher = QTeacher.teacher;

        if (id > 0) {

            // spec = spec.and(FlightSpecifications.hasId(id));
            predicates.add(teacher.id.eq(id));
        }

        if (StringUtils.hasText(name)) // SpringFramework-ös StringUtils
            // spec = spec.and(FlightSpecifications.hasFlightNumber(flightNumber));
            predicates.add(teacher.name.startsWithIgnoreCase(name));




        //return flightRepository.findAll(spec, Sort.by("id"));
        return Lists.newArrayList(teacherRepository.findAll(ExpressionUtils.allOf(predicates)));
    }

    public List<Teacher> findAll() {
        // return new ArrayList<>(airports.values());
        // return em.createQuery("SELECT a from Airport a", Airport.class).getResultList();

        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(long id) {
        //return airports.get(id);
        //return em.find(Airport.class, id);
        return teacherRepository.findById(id);
    }

    @Transactional
    public void delete(long id) {
        // em.remove(findById(id));
        // airports.remove(id);
        teacherRepository.deleteById(id);
    }

    @Transactional
    public Teacher update(Teacher teacher) {
        // airports.put(id, airport);
        //checkUniqueIata(airport.getIata(), airport.getId());
        if (teacherRepository.existsById(teacher.getId())) {
            //return airport;
            //return em.merge(airport); SD--->

//            logEntryService.createLog(String.format("Airport modified with id %d new name is %s",
//                    airport.getId(),
//                    airport.getName()));
//
//            callBackendSystem(); // mesterséges hiba generátor
            return teacherRepository.save(teacher);
        } else
            throw new NoSuchElementException();
    }

}
