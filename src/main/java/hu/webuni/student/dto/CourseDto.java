package hu.webuni.student.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CourseDto {


    private long id;

    //@Size(min = 3, max = 20)
    private String name;

    private Set<StudentDto> students;

    private Set<TeacherDto> teachers;
}
