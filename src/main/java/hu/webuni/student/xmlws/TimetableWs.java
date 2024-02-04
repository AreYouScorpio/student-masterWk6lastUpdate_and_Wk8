package hu.webuni.student.xmlws;

import hu.webuni.student.api.model.TimeTableItemDto;
import jakarta.jws.WebService;

import java.time.LocalDate;
import java.util.List;

@WebService
public interface TimetableWs {

    public  List<TimeTableItemDto> getTimeTableForStudent(Integer studentId, LocalDate from, LocalDate until);

}
