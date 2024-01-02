package hu.webuni.student.web;

import hu.webuni.student.api.ImageControllerApi;
import hu.webuni.student.model.Image;
import hu.webuni.student.model.Student;
import hu.webuni.student.repository.ImageRepository;
import hu.webuni.student.repository.StudentRepository;
import javassist.bytecode.ByteArray;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageController implements ImageControllerApi {

    private final ImageRepository imageRepository;

    private final StudentRepository studentRepository;


    @Override
    public ResponseEntity<Resource> downloadImage(Long id) {
        Optional<String> imageLocationOptional = getImageLocationForStudent(id);

        if (imageLocationOptional.isEmpty()) {
            System.out.println("No image exists");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String imageLocation = imageLocationOptional.get();
        Path imageFilePath = Paths.get(imageLocation);

        if (Files.exists(imageFilePath) && Files.isReadable(imageFilePath)) {
            Resource resource = new FileSystemResource(imageFilePath.toFile());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Or handle the error differently
        }
    }

    // Method to get student's image location for a student from DB
    public Optional<String> getImageLocationForStudent(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return Optional.ofNullable(student.getImageLocation());
        }
        return Optional.empty();
    }



}
