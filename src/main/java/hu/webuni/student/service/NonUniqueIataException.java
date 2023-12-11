package hu.webuni.student.service;

public class NonUniqueIataException extends RuntimeException{

    public NonUniqueIataException(String iata) {
        super("Existing IATA: "+ iata);
    }
}
