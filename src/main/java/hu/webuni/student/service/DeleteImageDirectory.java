package hu.webuni.student.service;

import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class DeleteImageDirectory {

    public void deleteAllFiles() {

        String directoryPath = "C:\\Users\\atschweger\\IDEA_projects\\student-masterWk3\\src\\main\\resources\\static\\images\\";
        System.out.println("Deleting image directory of \n" + directoryPath);

        try {
            // List all files in the directory
            Stream<Path> paths = Files.list(Paths.get(directoryPath));

            // Delete each file in the directory
            paths.forEach(path -> {
                try {
                    Files.delete(path);
                    System.out.println("Deleted file: " + path);
                } catch (IOException e) {
                    System.err.println("Failed to delete file: " + path + " - " + e.getMessage());
                }
            });

            System.out.println("All files deleted successfully.");
        } catch (IOException e) {
            System.err.println("Failed to list files in directory: " + directoryPath + " - " + e.getMessage());
        }

    }
}
