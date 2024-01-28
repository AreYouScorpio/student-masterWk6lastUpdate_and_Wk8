package hu.webuni.student.mapper;

import hu.webuni.student.api.model.TimeTableItemDto;
import hu.webuni.student.model.TimeTableItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeTableMapper {

    @Mapping(target = "courseName", source="course.name")
    public TimeTableItemDto timeTableItemToDto(TimeTableItem item);

    public List<TimeTableItemDto> timeTableItemsToDtos(List<TimeTableItem> items);

}
