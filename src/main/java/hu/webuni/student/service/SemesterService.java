package hu.webuni.student.service;

import hu.webuni.student.aspect.Retry;
import hu.webuni.student.error.ErrorDecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

//@LogCall - moved only to error method in ErrorDecision
@Service
public class SemesterService {

    private Random random = new Random();

    @Autowired
    ErrorDecision errorDecision;


    @Retry
    public int getFreeSemester(long centralId) {
        System.out.println("getFreeSemester called");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        int result = random.nextInt(0, 6);


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



}
