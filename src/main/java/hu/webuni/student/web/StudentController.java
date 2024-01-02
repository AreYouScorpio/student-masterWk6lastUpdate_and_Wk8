package hu.webuni.student.web;

import hu.webuni.student.api.StudentControllerApi;
import hu.webuni.student.api.model.StudentDto;
import hu.webuni.student.mapper.StudentMapper;
import hu.webuni.student.model.Image;
import hu.webuni.student.model.Student;
import hu.webuni.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class StudentController implements StudentControllerApi {

    //https://mapstruct.org/ minták !!! és pom.xml --- https://mapstruct.org/documentation/installation/

    //@Value("${upload.dir}")
    private String uploadDir;
    private final NativeWebRequest nativeWebRequest;

    private final ResourceLoader resourceLoader;

    // Constructor injection

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
        /*
        try {
            byte[] imageData = readImageData(content.getInputStream());
        Image image = studentService.saveImageForStudent(id, fileName, imageData);
        return  ResponseEntity.ok("/api/images/" + image.getId());}
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

         */

        /* working .. saving the whole file into DB:
        try {
            Image image = studentService.saveImageForStudent(id, fileName, content.getBytes());
            return  ResponseEntity.ok("/api/images/" + image.getId());}
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

         */


    /* streaming into DB not yet tested
    private byte[] readImageData(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
        */

        /* this is for saving into file + saving the file location into DB for student -> */






 {
            if (content.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file");
            }

            try {
                String filePath = getResourceFilePath(fileName);
                File targetFile = new File(filePath);

                // Check if an image already exists for the student
                String existingImagePath = studentService.getImageLocationForStudent(id);
                if (existingImagePath != null) {
                    File existingFile = new File(existingImagePath);
                    if (existingFile.exists()) {
                        existingFile.delete(); // Delete the existing image
                    }
                }

                // Save the new file to the server
                try (InputStream inputStream = content.getInputStream();
                     OutputStream outputStream = new FileOutputStream(targetFile)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                // Save the new file location into the database for the student
                studentService.saveImageLocationForStudent(id, filePath);

                return ResponseEntity.ok("File uploaded successfully: " + filePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
            }
        }



    }



    // Method to get the resource file path
    public String getResourceFilePath(String fileName) {
        try {
            // Define the fixed directory path
            String directoryPath = "src/main/resources/static/images";

            // Create a Path object for the directory
            Path directory = Paths.get(directoryPath);

            // Check if the directory exists; if not, create it
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Resolve the file path within the directory
            Path filePath = directory.resolve(fileName);

            // Return the absolute file path as a string
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Error accessing resource file: " + fileName, e);
        }
    }


}
