package hu.webuni.student.service;

import hu.webuni.student.model.Course;
import hu.webuni.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;


@Service
public class ReportGenerator {

    @Autowired
            CourseService courseService;






    Double averageSemester;
    //@Async
    //public CompletableFuture<Double> getReportAverage(long id) {
    public Double getReportAverage(long id) {


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

        System.out.println("Report done for CourseId " + id + ".");
        if (averageSemester.isPresent()) {
            double average = averageSemester.getAsDouble();
            System.out.println("Average semester: " + average + "\n");
        } else {
            System.out.println("No students in the course.\n");
        }

        //return CompletableFuture.completedFuture(averageSemester.getAsDouble());
        return averageSemester.getAsDouble();
    }
}
