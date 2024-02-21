package hu.webuni.student.jms;

import hu.webuni.jms.dto.FreeSemesterResponse;
import hu.webuni.student.model.Student;
import hu.webuni.student.repository.StudentRepository;
import hu.webuni.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FreeSemesterResponseConsumer {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private Student consumerStudent;

    //fogadja is a valaszt
    @JmsListener(destination = "free_semester_responses", containerFactory = "educationFactory")
    //dest = ahova a converandsend kuldi topicra, onnet kapjuk .. ide a containerFactory kell, nem a connectionFactory !
    public void onFreeSemesterResponse(FreeSemesterResponse response) { //itt eleg a payload, nem kell a header
        System.out.println("FreeSemesterResponseConsumer.java JMSlistener running");
        int freeSemesterGotByJms = response.getNumFreeSemesters();
        long idGotByJms = response.getStudentId();
        System.out.println("This is the FreeSemesterResponseConsumer/onFreeSemesterResponse freeSemester to setup: " + freeSemesterGotByJms);
        System.out.println("This is the FreeSemesterResponseConsumer/onFreeSemesterResponse StudentId for setup: " + idGotByJms);
        /*
        Optional<Student> consumerStudentOptional = Optional.of(studentRepository.getById(idGotByJms));
        if (!consumerStudentOptional.isEmpty()) {
            consumerStudent = consumerStudentOptional.get();
            System.out.println("This is the Student to setup: " + consumerStudent.toString());
            consumerStudent.setFreeSemester(freeSemesterGotByJms);
            studentRepository.save(consumerStudent);
        } else {
            System.out.println("FreeSemesterResponseConsumer/onFreeSemesterResponse consumerStudentOptional is empty.");
        }

         */

        studentService.updateFreeSemesters(response.getStudentId(), response.getNumFreeSemesters());

        //itt ne hivodjon ujra a freesemester call, hanem csak elmenteni db-ben, ahogy a tanar javasolta Wk7 v1 utan
    }
}
