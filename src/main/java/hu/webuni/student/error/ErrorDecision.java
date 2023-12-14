package hu.webuni.student.error;

import hu.webuni.student.aspect.LogCall;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ErrorDecision {

    public void throwError() {

        System.out.println("errorDecision called");
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error, not found."
        );

    }


}
