package hu.webuni.student.web;

import hu.webuni.student.api.TeacherControllerApi;
import hu.webuni.student.api.model.GetTeacherById200Response;
import hu.webuni.student.api.model.TeacherDto;
import hu.webuni.student.mapper.TeacherMapper;
import hu.webuni.student.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TeacherController implements TeacherControllerApi {

    private final NativeWebRequest nativeWebRequest;
    @Autowired
    TeacherService teacherService;

    @Autowired
    TeacherMapper teacherMapper;


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);    }

    @Override
    public ResponseEntity<TeacherDto> createTeacher(TeacherDto teacherDto) {
        return TeacherControllerApi.super.createTeacher(teacherDto);
    }

    @Override
    public ResponseEntity<Void> deleteTeacher(Long id) {
        return TeacherControllerApi.super.deleteTeacher(id);
    }

    @Override
    public ResponseEntity<List<TeacherDto>> getAllTeacher() {
        return TeacherControllerApi.super.getAllTeacher();
    }

    @Override
    public ResponseEntity<GetTeacherById200Response> getTeacherById(Long id) {
        return TeacherControllerApi.super.getTeacherById(id);
    }

    @Override
    public ResponseEntity<TeacherDto> modifyTeacher(Long id, TeacherDto teacherDto) {
        return TeacherControllerApi.super.modifyTeacher(id, teacherDto);
    }

    @Override
    public ResponseEntity<List<TeacherDto>> searchTeachers(TeacherDto teacherDto) {
        return TeacherControllerApi.super.searchTeachers(teacherDto);
    }
}
