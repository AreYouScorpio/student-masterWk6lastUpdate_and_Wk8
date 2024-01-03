package hu.webuni.student.web;

import hu.webuni.student.service.ReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportGenerator reportGenerator;

    @GetMapping("/api/courses/{id}/averageSemesterOfAllStudentsEnrolledForThisCourse/")
    public String getReportForCourse(@PathVariable long id){
        System.out.println("ReportGenerator.getReport called at thread " + Thread.currentThread().getName());
        return reportGenerator.getReport(id);
    }


}
