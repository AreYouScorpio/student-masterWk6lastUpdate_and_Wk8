package hu.webuni.student.web;

import hu.webuni.student.api.ImageControllerApi;
import hu.webuni.student.model.Student;
import hu.webuni.student.repository.ImageRepository;
import hu.webuni.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
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
        String filePath = getImageLocationForStudent(id).get().toString();

        if (filePath == null || !Files.exists(Paths.get(filePath))) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Open an input stream to the file
            FileInputStream inputStream = new FileInputStream(filePath);

            // Create a byte buffer for streaming with a size of 1024 bytes
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Create response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg");
            headers.setContentType(MediaType.IMAGE_JPEG);

            // Create a ByteArrayResource to store the streamed content in memory
            ByteArrayResource resource = new ByteArrayResource(new byte[0]);

            // Read the file content into the buffer and append it to the ByteArrayResource
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] currentBuffer = new byte[bytesRead];
                System.arraycopy(buffer, 0, currentBuffer, 0, bytesRead);
                //buffer: source array from which data will be copied
                //srcPos: starting position in the source array from where the data will be copied
                //currentBuffer: destination array
                //destPos: starting position in the destination array where the data will be copied
                resource = concatenateByteArrayResources(resource, new ByteArrayResource(currentBuffer));
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

    // Method to concatenate ByteArrayResources
    private ByteArrayResource concatenateByteArrayResources(ByteArrayResource resource1, ByteArrayResource resource2) {
        byte[] byteArray1 = resource1.getByteArray();
        byte[] byteArray2 = resource2.getByteArray();
        byte[] result = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, result, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, result, byteArray1.length, byteArray2.length);
        return new ByteArrayResource(result);
    }



}
