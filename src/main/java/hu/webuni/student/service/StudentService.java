package hu.webuni.student.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import hu.webuni.student.model.QStudent;
import hu.webuni.student.model.Student;
import hu.webuni.student.repository.CourseRepository;
import hu.webuni.student.repository.ImageRepository;
import hu.webuni.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final ImageRepository imageRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    SemesterService semesterService;

    @Autowired
    TaskScheduler taskScheduler;

    private Map<Long, ScheduledFuture<?>> pollerJobs = new ConcurrentHashMap<>(); // mert tobbszalon meghivodhat, ezert concurrent hashmap


    @Transactional
    public Student save(Student student) {
        //a takeoff/landing airportból csak az id-t vesszük figyelembe, már létezniük kell
//        student.setTakeoff(studentRepository.findById(flight.getTakeoff().getId()).get());
//        student.setLanding(studentRepository.findById(flight.getLanding().getId()).get());
        return studentRepository.save(student);
    }

    public List<Student> findStudentsByExample(Student example) {
        long id = example.getId();
        String name = example.getName();

        LocalDate birthdate = example.getBirthdate();

            int semester = example.getSemester();

            //Specification<Flight> spec = Specification.where(null); // üres Specification, ami semmire nem szűr

        // a FlightSpecifications feltételeit ide írjuk bele közvetlen --->

        ArrayList<Predicate> predicates = new ArrayList<Predicate>();

        QStudent student = QStudent.student;

        if (id > 0) {

            // spec = spec.and(FlightSpecifications.hasId(id));
            predicates.add(student.id.eq(id));
        }

        if (StringUtils.hasText(name)) // SpringFramework-ös StringUtils
            // spec = spec.and(FlightSpecifications.hasFlightNumber(flightNumber));
            predicates.add(student.name.startsWithIgnoreCase(name));


        if (semester >= 1) // SpringFramework-ös StringUtils
        //spec = spec.and(FlightSpecifications.hasTakeoffTime(takeoffTime));
        {

            predicates.add(student.semester.between(semester, semester+1));

        }


        //return flightRepository.findAll(spec, Sort.by("id"));
        return Lists.newArrayList(studentRepository.findAll(ExpressionUtils.allOf(predicates)));
    }

    public List<Student> findAll() {
        // return new ArrayList<>(airports.values());
        // return em.createQuery("SELECT a from Airport a", Airport.class).getResultList();

        return studentRepository.findAll();
    }

    public Optional<Student> findById(long id) {
        //return airports.get(id);
        //return em.find(Airport.class, id);
        return studentRepository.findById(id);
    }

    public Student findStudentByCentralId(int centralId) {
        return studentRepository.findByCentralId(centralId);
    }

    @Transactional
    public void delete(long id) {
        // em.remove(findById(id));
        // airports.remove(id);
        studentRepository.deleteById(id);
    }

    @Transactional
    public Student update(Student student) {
        // airports.put(id, airport);
        //checkUniqueIata(airport.getIata(), airport.getId());
        if (studentRepository.existsById(student.getId())) {
            //return airport;
            //return em.merge(airport); SD--->

//            logEntryService.createLog(String.format("Airport modified with id %d new name is %s",
//                    airport.getId(),
//                    airport.getName()));
//
//            callBackendSystem(); // mesterséges hiba generátor
            return studentRepository.save(student);
        } else
            throw new NoSuchElementException();
    }


    @Transactional
    //@Scheduled(cron = "*/15 * * * * *") //15mpenkent -> ehelyett file-bol olvasni
    @Scheduled(cron = "${scheduler.cron}")
    @Async
    public void updateSemesters(){
        System.out.println("updateSemesters called");

        // by centralId

        System.out.println(studentRepository.findAll().toString());
        studentRepository.findAll().forEach(student ->
        {
            Integer centralIdOfActualStudent = student.getCentralId();
            System.out.println("centralIdOfActualStudent: " + centralIdOfActualStudent);
            if (centralIdOfActualStudent!=null) {
                student = studentRepository.findByCentralId(centralIdOfActualStudent);
                System.out.println("This is the student for this centralId: " + student.toString());
            updateStudentWithSemester(student);}
        });



        // by id
/*
        studentRepository.findAll().forEach(student ->
        {
            updateStudentWithSemester(student);
        });


 */
    }

    private void updateStudentWithSemester(Student student) {
        try {
        student.setFreeSemester(semesterService.getFreeSemester(student.getCentralId()));
        studentRepository.save(student);
            System.out.println("The new student saved: " + student.toString());
        }
        catch (Exception e) {
            System.out.println("Error catched for id: "+ student.getId() + " and centralId: " +student.getCentralId() +" in StudentService/updateStudentWithSemester() ..startPolling" );
            startPollingForSemester(student.getId(), 500);
            stopDelayPollingForSemester(student.getId()); // hogy ne legyen polling es adatmentes, ha mar a hiba helyett jott megfelelo eredmeny, kulonben allandoan probalna updatelni a hibat dobo student freeSemester-et
        }
    }


    public void startPollingForSemester(long id, long rate){ // cron helyett fixed rate scheduling lesz
        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(() -> {
            Optional<Student> studentOptional = studentRepository.findById(id);
            System.out.println("startPollingForSemester starts @ id: "+id + " with rate: " + rate);
            if (studentOptional.isPresent())
                updateStudentWithSemester(studentOptional.get());
        }, rate);
        stopDelayPollingForSemester(id); // leallitani, nehogy meg egyet inditsunk ref nelkul, ha nem lett leallitva elozo
        pollerJobs.put(id, scheduledFuture);
        //System.out.println("actual PollerJobs map: " + pollerJobs.toString());
    }

    public void stopDelayPollingForSemester(long id){
        System.out.println("stopPollingForStudent for id: " + id);
        ScheduledFuture<?> scheduledFuture = pollerJobs.get(id);
        if(scheduledFuture!=null)
            scheduledFuture.cancel(false); // ha epp futasban van, akarjuk-e megszakitani, azt nem akarjuk
    }


    @Transactional
    //OLD public Image saveImageForStudent(long studentId, String fileName, byte[] bytes) {
    public void saveImageForStudent(long studentId, String fileName, InputStream bytes) {

        if (!studentRepository.existsById(studentId)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        try{
            Student student = studentRepository.findById(studentId).get();
            Files.copy(bytes, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);}
        catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        /* OLD starts ->
        Student student = studentRepository.findById(studentId).get();
        Image image = Image.builder()
                .data(bytes)
                .fileName(fileName)
                .build();
        image = imageRepository.save(image);
        student.setImage(image);
        return image;

        -> OLD ends */


    }

    public String getImageLocationForStudent(Long studentId) {
        // Implement logic to retrieve the image location for the given student ID from the database
        // This assumes that each student has a field to store the image location, adjust accordingly based on your database structure
        return studentRepository.findById(studentId)
                .map(Student::getImageLocation)
                .orElse(null);
    }

    public void saveImageLocationForStudent(Long studentId, String filePath) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setImageLocation(filePath);
            studentRepository.save(student);
        } else {
            // Handle scenario where student with given ID is not found
            throw new NoSuchElementException("Student not found with ID: " + studentId);
        }
    }
}
