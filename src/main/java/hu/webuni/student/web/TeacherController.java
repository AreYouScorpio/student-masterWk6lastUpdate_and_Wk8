package hu.webuni.student.web;

import hu.webuni.student.dto.TeacherDto;
import hu.webuni.student.mapper.TeacherMapper;
import hu.webuni.student.model.Teacher;
import hu.webuni.student.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    //https://mapstruct.org/ minták !!! és pom.xml --- https://mapstruct.org/documentation/installation/

    @Autowired
    TeacherService teacherService;

    @Autowired
    TeacherMapper teacherMapper;

    //@Autowired
    //LogEntryService logEntryService;

    @GetMapping
    public List<TeacherDto> getAllTeacher() {
        return teacherMapper.teachersToDtos(teacherService.findAll());
    }


    @GetMapping("/{id}")
    public TeacherDto getTeacherById(@PathVariable long id) {
        Teacher teacher = teacherService.findById(id)
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
        return teacherMapper.teacherToDto(teacher);

    }


    @PostMapping
    public TeacherDto createTeacher(@RequestBody @Valid TeacherDto teacherDto /*, BindingResult errors */) {
        //if (errors.hasErrors()) throw new ...


        // áthelyezve mapper bevezetésével a service-be:
        // checkUniqueIata(airportDto.getIata());

        Teacher teacher = teacherService.save(teacherMapper.dtoToTeacher(teacherDto));
        // szintén törölve áthelyezés miatt --> airports.put(airportDto.getId(), airportDto);
        // return airportDto; --->
        return teacherMapper.teacherToDto(teacher);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable long id) {
        teacherService.delete(id);
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
    public ResponseEntity<TeacherDto> modifyTeacher(@PathVariable long id,
                                                    @RequestBody TeacherDto teacherDto) {

        Teacher teacher = teacherMapper.dtoToTeacher(teacherDto);
        teacher.setId(id); // hogy tudjunk módosítani azonos iata-jút a uniqecheck ellenére
        try {
            TeacherDto savedTeacherDto = teacherMapper.teacherToDto(teacherService.update(teacher));

            // LogEntryRepository.save(new LogEntry("Airport modified with id " + id)); -- service hozzáadva
            // logEntryService.createLog("Airport modified with id " + id); -inkább a service update legyen felelős érte, h a logot lementse
            // a service autowired-et is lehet így innét törölni, átvinni AirportService-be


            return ResponseEntity.ok(savedTeacherDto);
        }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/search")
    public List<TeacherDto> searchTeachers(@RequestBody TeacherDto example){



        return teacherMapper.teachersToDtos(teacherService.findTeachersByExample(teacherMapper.dtoToTeacher(example)));
    }



}
