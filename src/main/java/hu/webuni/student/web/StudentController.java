package hu.webuni.student.web;

import hu.webuni.student.api.StudentControllerApi;
import hu.webuni.student.api.model.StudentDto;
import hu.webuni.student.mapper.StudentMapper;
import hu.webuni.student.model.Image;
import hu.webuni.student.model.Student;
import hu.webuni.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class StudentController implements StudentControllerApi {

    //https://mapstruct.org/ minták !!! és pom.xml --- https://mapstruct.org/documentation/installation/


    private final NativeWebRequest nativeWebRequest;
    @Autowired
    StudentService studentService;


    @Autowired
    StudentMapper studentMapper;

    //@Autowired
    //LogEntryService logEntryService;


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<Void> deleteStudent(Long id) {
        studentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<StudentDto> getStudentById(Long id) {
        //System.out.println("Hello id");

        Student student = studentService.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(studentMapper.studentToDto(student));
    }

    @Override
    public ResponseEntity<StudentDto> modifyStudent(Long id, StudentDto studentDto) {
        Student student = studentMapper.dtoToStudent(studentDto);
        student.setId(id); // hogy tudjunk módosítani azonos iata-jút a uniqecheck ellenére
        try {
            StudentDto savedStudentDto = studentMapper.studentToDto(studentService.update(student));

            return ResponseEntity.ok(savedStudentDto);
        }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<StudentDto> createStudent(StudentDto studentDto) {
        Student student = studentService.save(studentMapper.dtoToStudent(studentDto));
        return ResponseEntity.ok(studentMapper.studentToDto(student));
    }

    @Override
    public ResponseEntity<List<StudentDto>> getAllStudent() {
        //System.out.println("Hello all");
        return ResponseEntity.ok(studentMapper.studentsToDtos(studentService.findAll()));
    }

    @Override
    public ResponseEntity<List<StudentDto>> searchStudents(StudentDto example) {
        return ResponseEntity.ok(studentMapper.studentsToDtos(studentService.findStudentsByExample(studentMapper.dtoToStudent(example))));

    }

    @Override
    public ResponseEntity<String> uploadImageForStudent(Long id, String fileName, MultipartFile content)  {
        try {
            byte[] imageData = readImageData(content.getInputStream());
        Image image = studentService.saveImageForStudent(id, fileName, imageData);
        return  ResponseEntity.ok("/api/images/" + image.getId());}
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private byte[] readImageData(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }




}
