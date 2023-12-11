package hu.webuni.student.mapper;

import hu.webuni.student.dto.StudentDto;
import hu.webuni.student.model.Student;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface StudentMapper {

    List<StudentDto> studentsToDtos(List<Student> students);

    StudentDto studentToDto(Student student);

    Student dtoToStudent(StudentDto studentDto);
}

        /*

        https://mapstruct.org/ ----> example:

        CarMapper INSTANCE = Mappers.getMapper( CarMapper.class ); 3

        @Mapping(source = "numberOfSeats", target = "seatCount")
        CarDto carToCarDto(Car car); 2

         */

