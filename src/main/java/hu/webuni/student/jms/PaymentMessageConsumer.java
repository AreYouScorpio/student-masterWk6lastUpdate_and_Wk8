package hu.webuni.student.jms;

import hu.webuni.spring.financialsystem.model.Payment;
import hu.webuni.student.model.Student;
import hu.webuni.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentMessageConsumer {

    @Autowired
    StudentService studentService;

    //behivatkozni jmsconfigbol, ami a csatlakozast biztositja factory (myFactory)
    @JmsListener(destination = "payments", containerFactory = "myFactory")
    //queue or topic name (the same at financialsystem) //ehhez meg kell egy converter is, h Payment object-et tudjon gyartani
    public void onPaymentMessage(Payment paymentMessage) {
        System.out.println("This is the paymentMessage: " + paymentMessage.toString() + " values: " + paymentMessage.getStudentId() + "  " + paymentMessage.getCashPaidIn());


        // Retrieve the student from the database
        Optional<Student> optionalStudent = studentService.findById(paymentMessage.getStudentId());


        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            // Update the cashPaidIn value
            int actualBalance = student.getCashPaidIn();
            System.out.println("Previous balance: " + actualBalance);
            int newBalance = actualBalance + paymentMessage.getCashPaidIn();
            System.out.println("Updated balance: " + newBalance);

            student.setCashPaidIn(newBalance);

            // Save the updated student entity to the database
            studentService.save(student);
        } else {
            System.out.println("Student not found with ID: " + paymentMessage.getStudentId());
        }





    }

}
