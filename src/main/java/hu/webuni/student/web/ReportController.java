package hu.webuni.student.web;

import hu.webuni.student.service.ReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportGenerator reportGenerator;

    @Async
    @GetMapping("/api/courses/{id}/averageSemesterOfAllStudentsEnrolledForThisCourse/")
    public CompletableFuture<Double> getReportForCourse(@PathVariable long id){
        System.out.println("ReportGenerator.getReport called at thread " + Thread.currentThread().getName());
        return CompletableFuture.completedFuture(reportGenerator.getReportAverage(id));
    }


}
