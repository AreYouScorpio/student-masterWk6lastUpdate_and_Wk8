package hu.webuni.student.xmlws;

import hu.webuni.student.api.model.TimeTableItemDto;
import hu.webuni.student.mapper.TimeTableMapper;
import hu.webuni.student.model.TimeTableItem;
import hu.webuni.student.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TimetableWsImpl implements TimetableWs{

    private final TimeTableService timeTableService;
    private final TimeTableMapper timeTableMapper;

    @Override
    public List<TimeTableItemDto> getTimeTableForStudent(Integer studentId, LocalDate from, LocalDate until) {
        try {
            if (studentId != null) {
                ArrayList<TimeTableItemDto> result = new ArrayList<>();

                Map<LocalDate, List<TimeTableItem>> timeTableForStudent = timeTableService
                        .getTimeTableForStudent(studentId, from, until);

                for (Map.Entry<LocalDate, List<TimeTableItem>> entry : timeTableForStudent.entrySet()) {
                    LocalDate day = entry.getKey();
                    List<TimeTableItem> items = entry.getValue();
                    List<TimeTableItemDto> itemDtos = timeTableMapper.timeTableItemsToDtos(items);
                    itemDtos.forEach(i -> i.setDay(day));
                    result.addAll(itemDtos);
                }
                return result;
            }

            else
                return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }

    }




}
