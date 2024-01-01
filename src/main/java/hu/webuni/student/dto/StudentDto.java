package hu.webuni.student.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentDto {

    private long id;

    //@Size(min = 3, max = 20)
    private String name;
    //@Size (min = 3, max = 10)
    private LocalDate birthdate;
    private int semester;
    private int centralId;
    private int freeSemester;

}
