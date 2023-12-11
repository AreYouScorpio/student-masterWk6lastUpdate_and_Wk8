package hu.webuni.student.mapper;

import hu.webuni.student.dto.TeacherDto;
import hu.webuni.student.model.Teacher;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TeacherMapper {

    List<TeacherDto> teachersToDtos(List<Teacher> teachers);

    TeacherDto teacherToDto(Teacher teacher);

    Teacher dtoToTeacher(TeacherDto teacherDto);
}

        /*

        https://mapstruct.org/ ----> example:

        CarMapper INSTANCE = Mappers.getMapper( CarMapper.class ); 3

        @Mapping(source = "numberOfSeats", target = "seatCount")
        CarDto carToCarDto(Car car); 2

         */

