package hu.webuni.student.mapper;

import hu.webuni.student.dto.CourseDto;
import hu.webuni.student.model.Course;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CourseMapper {


    CourseDto courseToDto(Course course);

    Iterable<CourseDto> coursesToDtos(Iterable<Course> courses);

    Course dtoToCourse(CourseDto courseDto);

    Iterable<Course> dtosToCourses(Iterable<CourseDto> courseDtoIterable);


    @Named("summary")
    @Mapping(ignore = true, target = "teachers")
    @Mapping(ignore = true, target = "students")
    CourseDto courseSummaryToDto(Course course);

    @IterableMapping(qualifiedByName = "summary")
    List<CourseDto> courseSummariesToDtos(Iterable<Course> courses);






}

        /*

        https://mapstruct.org/ ----> example:

        CarMapper INSTANCE = Mappers.getMapper( CarMapper.class ); 3

        @Mapping(source = "numberOfSeats", target = "seatCount")
        CarDto carToCarDto(Car car); 2

         */



