package hu.webuni.student.jms;

import hu.webuni.jms.dto.FreeSemesterResponse;
import hu.webuni.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FreeSemesterResponseConsumer {

    private final StudentService studentService;

    @JmsListener(destination = "free_semester_responses", containerFactory = "educationFactory") //dest = ahova a converandsend kuldi topicra, onnet kapjuk
    public void onFreeSemesterResponse(FreeSemesterResponse response) {
        studentService.updateStudentWithSemester(response.getStudentId(), response.getNumFreeSemesters());
    }
}
