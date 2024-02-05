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

    //fogadja is a valaszt
    @JmsListener(destination = "free_semester_responses", containerFactory = "educationFactory") //dest = ahova a converandsend kuldi topicra, onnet kapjuk .. ide a containerFactory kell, nem a connectionFactory !
    public void onFreeSemesterResponse(FreeSemesterResponse response) { //itt eleg a payload, nem kell a header
        studentService.updateStudentWithSemester(response.getStudentId(), response.getNumFreeSemesters());
    }
}
