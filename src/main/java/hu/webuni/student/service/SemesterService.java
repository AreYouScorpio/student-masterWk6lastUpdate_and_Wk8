package hu.webuni.student.service;

import hu.webuni.student.aspect.Retry;
import hu.webuni.student.error.ErrorDecision;
import hu.webuni.jms.dto.FreeSemesterRequest;
import hu.webuni.student.repository.StudentRepository;
import hu.webuni.student.wsclient.CentralsystemXmlWs;
import hu.webuni.student.wsclient.CentralsystemXmlWsImplService;
import jakarta.jms.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

//@LogCall - moved only to error method in ErrorDecision
@Service
@RequiredArgsConstructor
public class SemesterService {

    private Random random = new Random();

    @Autowired
    ErrorDecision errorDecision;

    private final JmsTemplate educationJmsTemplate; // kell egy template Wk7

    @Autowired
    StudentRepository studentRepository;

    @Retry
    public int getFreeSemester(long centralId) {
        System.out.println("getFreeSemester called");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        // local version:
        int result = random.nextInt(0, 6);
        System.out.println("FreeSemester(s) got from SemesterService/getFreeSemester locally: " + result);
        // remote version / XML from centralSystem server on 8081:
        CentralsystemXmlWs centralsystemXmlWsImplPort = new CentralsystemXmlWsImplService().getCentralsystemXmlWsImplPort(); // a port az interface szinonimaja a wsdl-ben
        result = centralsystemXmlWsImplPort.getFreeSemesterByCentralId(1);
        System.out.println("FreeSemester(s) got from centralSystem remotely: " + result);

        //for testing, mocking testNumber++:

//        testNumber++;
//        int result = testNumber;
//        System.out.println("TestNumber " + testNumber);



        System.out.println("randomFreeSemester for centralId (" + centralId +") : " + result);
        if (Math.random() > 0.5) return result;

        //for testing, mocking testNumber++:
        //if (testNumber>2 || testNumber<2) return result;


        else {
            errorDecision.throwError();
//            int resultNew = -1;
//            int triedConnection = 1;
//
//            while (resultNew==-1 && triedConnection<6) {
//                resultNew = scheduleFixedRateTask();
//                System.out.println(
//                        "Fixed rate task started - " + System.currentTimeMillis() / 1000);
//                triedConnection++;
//            }
            return -1;
        }
    }

//    @Scheduled(fixedRate = 500)
//    @Async
//    public int scheduleFixedRateTask() {
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//        }
//
//        System.out.println("New result after nth connection: "+ result );
//        return result;
//    }

    public void askNumFreeSemestersForStudent(int eduId) {

        long studentId = studentRepository.findByCentralId(eduId).getId();
        Topic topic = educationJmsTemplate.execute(session -> session.createTopic("free_semester_responses")); // topic letrehozasa hozza
        //az execute-tal lehet barmit megcsinalni
        //megkapja a jms sessiont, ami a belepesi pontja a jms apinak
        FreeSemesterRequest freeSemesterRequest = new FreeSemesterRequest();
        //freeSemesterRequest.setStudentId(eduId);
        freeSemesterRequest.setStudentId((int)studentId);

        System.out.println("eduId/centralId = " + eduId);
        System.out.println("StudentId based on eduId/centralId = " + studentId);

        educationJmsTemplate.convertAndSend("free_semester_requests",
                freeSemesterRequest,  //ezt kuldom uzenetet.. itt vege is lenne normal esetben: educationJmsTemplate.convertAndSend("free_semester_requests", freeSemesterRequest), de be kell allitani headert, a ReplyTo-ban akarom kuldeni az infot
               message -> { //megkapjuk a jms message-et
            message.setJMSReplyTo(topic); //beallitjuk rajta a ReplyTo mezot egy topic-ra .. a topic-ot itt par sorral feljebb letrehozzuk hozza
            return message; // mielott visszaterek magaval a message-el
        });


    }

}
