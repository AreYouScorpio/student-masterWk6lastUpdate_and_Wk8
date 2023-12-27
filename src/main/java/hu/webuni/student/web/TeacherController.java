package hu.webuni.student.web;

import hu.webuni.student.api.TeacherControllerApi;
import hu.webuni.student.api.model.GetTeacherById200Response;
import hu.webuni.student.api.model.TeacherDto;
import hu.webuni.student.mapper.TeacherMapper;
import hu.webuni.student.model.Teacher;
import hu.webuni.student.repository.TeacherRepository;
import hu.webuni.student.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TeacherController implements TeacherControllerApi {

    private final NativeWebRequest nativeWebRequest;
    @Autowired
    TeacherService teacherService;

    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    TeacherMapper teacherMapper;


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);    }

    @Override
    public ResponseEntity<TeacherDto> createTeacher(TeacherDto teacherDto) {
        Teacher teacher = teacherService.save(teacherMapper.dtoToTeacher(teacherDto));

        return ResponseEntity.ok(teacherMapper.teacherToDto(teacher));    }

    @Override
    public ResponseEntity<Void> deleteTeacher(Long id) {
        teacherService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<TeacherDto>> getAllTeacher() {
        return ResponseEntity.ok(teacherMapper.teachersToDtos(teacherService.findAll()));
    }

    @Override
    public ResponseEntity<GetTeacherById200Response> getTeacherById(Long id) {
        Teacher teacher = teacherService.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(teacherMapper.teacherToDto(teacher));

    }

    @Override
    public ResponseEntity<TeacherDto> modifyTeacher(Long id, TeacherDto teacherDto) {

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
        }    }

    @Override
    public ResponseEntity<List<TeacherDto>> searchTeachers(TeacherDto example) {
        return ResponseEntity.ok(teacherMapper.teachersToDtos(teacherService.findTeachersByExample(teacherMapper.dtoToTeacher(example))));
    }
}
