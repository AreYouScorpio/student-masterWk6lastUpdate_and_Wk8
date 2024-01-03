package hu.webuni.student.service;

import hu.webuni.student.model.Course;
import hu.webuni.student.model.Student;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.OptionalDouble;


@Service
public class ReportGenerator {

    @Autowired
            CourseService courseService;






    Double averageSemester;
    public String getReport(long id) {


        System.out.println("Report generator called. [ReportGenerator.getReport] at thread " +
                Thread.currentThread().getName());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        Course course = courseService.findById(id).get();
        OptionalDouble averageSemester = course.getStudents()
                .stream()
                .mapToInt(Student::getSemester) // Assuming 'getSemester' method returns the semester
                .average();

        if (averageSemester.isPresent()) {
            double average = averageSemester.getAsDouble();
            System.out.println("Average semester: " + average);
        } else {
            System.out.println("No students in the course.");
        }

        return "Report done for CourseId " + id + "\n ..the average of all enrolled student's semester is: " + averageSemester.getAsDouble();
    }
}
