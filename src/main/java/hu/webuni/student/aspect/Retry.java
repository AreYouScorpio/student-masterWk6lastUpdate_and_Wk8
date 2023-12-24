package hu.webuni.student.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //meddig el az annotacio, futasi idoben is felterkepezi a Spring, hol a logcall
@Target({ElementType.TYPE, ElementType.METHOD}) // target mire akarjuk ratenni, metodusokra es tipusokre (osztalyra, aminek osszes metodusat szeretnem pl loggolni)
public @interface Retry {
}
