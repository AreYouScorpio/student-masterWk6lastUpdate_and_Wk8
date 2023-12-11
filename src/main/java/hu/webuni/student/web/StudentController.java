package hu.webuni.student.web;

import hu.webuni.student.dto.StudentDto;
import hu.webuni.student.mapper.StudentMapper;
import hu.webuni.student.model.Student;
import hu.webuni.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    //https://mapstruct.org/ minták !!! és pom.xml --- https://mapstruct.org/documentation/installation/

    @Autowired
    StudentService studentService;

    @Autowired
    StudentMapper studentMapper;

    //@Autowired
    //LogEntryService logEntryService;

    @GetMapping
    public List<StudentDto> getAllStudent() {
        return studentMapper.studentsToDtos(studentService.findAll());
    }


    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable long id) {
        Student student = studentService.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));

        // deleted after mapper ---> AirportDto airportDto = airports.get(id);
//        if (airportDto!=null)
//            return ResponseEntity.ok(airportDto);
//        else
//        return ResponseEntity.notFound().build();
       /* ehelyett is orElseThrow és a return marad
        if (airport != null)
            return airportMapper.airportToDto(airport);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        */
        return studentMapper.studentToDto(student);

    }


    @PostMapping
    public StudentDto createStudent(@RequestBody @Valid StudentDto studentDto /*, BindingResult errors */) {
        //if (errors.hasErrors()) throw new ...


        // áthelyezve mapper bevezetésével a service-be:
        // checkUniqueIata(airportDto.getIata());

        Student student = studentService.save(studentMapper.dtoToStudent(studentDto));
        // szintén törölve áthelyezés miatt --> airports.put(airportDto.getId(), airportDto);
        // return airportDto; --->
        return studentMapper.studentToDto(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.delete(id);
    }


    /*

    @PutMapping("/{id}")
    public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id,
                                                    @RequestBody AirportDto airportDto) {
        if (!airports.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }

        checkUniqueIata(airportDto.getIata());
        airportDto.setId(id);
        airports.put(id, airportDto);
        return ResponseEntity.ok(airportDto);
    }
new PutMapping after MapStruct added:
---->

    */

    /* saját mego:

    @PutMapping("/{id}")
    public AirportDto modifyAirport(@PathVariable long id,
                                                    @RequestBody @Valid AirportDto airportDto) {

        Airport airport = airportService.findById(id);


        if (airport != null)
            airportService.update(id, airportMapper.dtoToAirport(airportDto));
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);


        return airportMapper.airportToDto(airport);


    }



     */


    // ---> ehelyett tanári megoldás, de enyém is működött ---->


    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> modifyStudent(@PathVariable long id,
                                                    @RequestBody StudentDto studentDto) {

        Student student = studentMapper.dtoToStudent(studentDto);
        student.setId(id); // hogy tudjunk módosítani azonos iata-jút a uniqecheck ellenére
        try {
            StudentDto savedStudentDto = studentMapper.studentToDto(studentService.update(student));

            // LogEntryRepository.save(new LogEntry("Airport modified with id " + id)); -- service hozzáadva
            // logEntryService.createLog("Airport modified with id " + id); -inkább a service update legyen felelős érte, h a logot lementse
            // a service autowired-et is lehet így innét törölni, átvinni AirportService-be


            return ResponseEntity.ok(savedStudentDto);
        }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/search")
    public List<StudentDto> searchStudents(@RequestBody StudentDto example){



        return studentMapper.studentsToDtos(studentService.findStudentsByExample(studentMapper.dtoToStudent(example)));
    }



}
