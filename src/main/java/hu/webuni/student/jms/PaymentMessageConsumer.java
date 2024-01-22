package hu.webuni.student.jms;

import hu.webuni.student.model.Payment;
import hu.webuni.student.model.Student;
import hu.webuni.student.service.StudentService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentMessageConsumer {

    @Autowired
    StudentService studentService;

    //behivatkozni jmsconfigbol, ami a csatlakozast biztositja factory (myFactory)
    @JmsListener(destination = "payments", containerFactory = "myFactory") //queue or topic name (the same at financialsystem) //ehhez meg kell egy converter is, h Payment object-et tudjon gyartani
    public void onPaymentMessage(Payment paymentMessage){
        System.out.println("This is the paymentMessage: " + paymentMessage.toString() + " values: "  + paymentMessage.getStudentId() + "  " + paymentMessage.getCashPaidIn());

        int actualBalance = studentService.findById(paymentMessage.getStudentId()).get().getCashPaidIn();
        System.out.println("Previous balance: " + actualBalance);
        int newBalance =+ actualBalance;
        System.out.println("Updated balance: " + newBalance);

        studentService.findById(paymentMessage.getStudentId()).get().setCashPaidIn(newBalance);


    }

}
