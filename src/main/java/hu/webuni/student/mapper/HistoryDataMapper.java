package hu.webuni.student.mapper;

import hu.webuni.student.api.model.CourseDto;
import hu.webuni.student.api.model.HistoryDataCourseDto;
import hu.webuni.student.model.Course;
import hu.webuni.student.model.HistoryData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoryDataMapper {

    HistoryDataCourseDto courseHistoryDataToDto(HistoryData<Course> courseHistoryData);
}
