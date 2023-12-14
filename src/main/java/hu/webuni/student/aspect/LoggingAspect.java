package hu.webuni.student.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    //@Around("execution(* hu.webuni.student.repository.*.*(..))") //barmely tipus(interface v osztaly) /barmely metodus/barmely parameter(..)
    @Pointcut("@annotation(hu.webuni.student.aspect.LogCall) || @within(hu.webuni.student.aspect.LogCall)") //mikor kell meghivodnia.. ha a metodusra ratesszuk a @LogCall annotaciot, akk hivodjon meg VAGY olyan tipuson belul van, amely tipuson rajta van a @LogCall annotacio (pl interface-en, v metoduson)
    public void annotationLogCall(){

    }


//
//    //@Before("execution(* hu.webuni.student.repository.*.*(..))")
//    @Before("hu.webuni.student.aspect.LoggingAspect.annotationLogCall()") // ez a metodus definialja a szabalyt, a pointcut-ot
//    //* mind1 melyik tipusban (barmely interface, vagy osztaly),
//    // * mindegy, h mi a metodus (barmely metodus),
//    // (..) mindegy, milyen parameterei vannak
//    public void logBefore(JoinPoint joinPoint){
//        Class<?> clazz = joinPoint.getTarget().getClass();
//        Class<?>[] interfaces = clazz.getInterfaces();
//        //vannak-e interface-k? -> ettol fugg, milyen tipust fogok kiirni (osztalynev, vagy 0. interface tostringje)
//        String type = interfaces.length == 0 ? clazz.getName() : interfaces[0].toString();
//
////        System.out.println(String.format("Method %s called in class %s",
////                joinPoint.getSignature(), //.getSignature melyik metodus, teljes signaturaja a metodusnak
////                type //.getTarget a cel osztaly, megkerdezzuk oszt nevet //getClass().getName() is lehetoseg, de akk generalt nevet ad
////                ));
//        // system.out.format kv, akk nekem kell a sortorest is kitenni a vegere %n
//
//                System.out.format("Method %s called in class %s%n",
//                joinPoint.getSignature(), //.getSignature melyik metodus, teljes signaturaja a metodusnak
//                type //.getTarget a cel osztaly, megkerdezzuk oszt nevet //getClass().getName() is lehetoseg, de akk generalt nevet ad
//                );
//
//
//
//    }

    //@Before("execution(* hu.webuni.student.service.*.*(..))")
    //@Before("hu.webuni.student.aspect.LoggingAspect.annotationLogCall()") // ez a metodus definialja a szabalyt, a pointcut-ot
    @Around("hu.webuni.student.aspect.LoggingAspect.annotationLogCall()")
    public Object logBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        int maxAttempts = 5;
        int attempts = 0;
        Throwable lastException = null;
        Object result = null;

        while (attempts < maxAttempts) {
            try {
                result = joinPoint.proceed();
                break;
            } catch (Throwable e) {
                lastException = e;
                System.out.println("Error occurred: " + e.getMessage());
                attempts++;
                if (attempts < maxAttempts) {
                    System.out.println("Retrying after 500ms...");
                    Thread.sleep(500);
                    // Clear the last exception caught before the next attempt
                    lastException = null;
                }
            }
        }

        if (lastException != null) {
            System.out.println("Maximum attempts reached. Exiting retry loop.");
            throw lastException;
        }

        return result;
    }
}
