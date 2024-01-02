package hu.webuni.student.web;

import hu.webuni.student.api.ImageControllerApi;
import hu.webuni.student.model.Image;
import hu.webuni.student.repository.ImageRepository;
import javassist.bytecode.ByteArray;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageController implements ImageControllerApi {

    private final ImageRepository imageRepository;

    @Override
    public ResponseEntity<Resource> downloadImage(Long id) {


        return ResponseEntity.ok(
                new ByteArrayResource(imageRepository.findById(id).get().getData())
        );

        /*
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isEmpty()) {
            System.out.println("No image exists");
            return ResponseEntity.notFound().build();
        }

        Image image = imageOptional.get();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image.getData());

        return ResponseEntity
                .ok()
                .contentLength(image.getData().length) // Set content length if known
                .contentType(MediaType.IMAGE_JPEG) // Set the appropriate media type
                .body(new InputStreamResource(inputStream));
    }


         */
}}
