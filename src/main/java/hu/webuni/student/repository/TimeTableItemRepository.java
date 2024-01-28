package hu.webuni.student.repository;

import hu.webuni.student.model.Semester;
import hu.webuni.student.model.TimeTableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeTableItemRepository  extends JpaRepository<TimeTableItem, Integer> {

    @Query("SELECT t FROM TimeTableItem t WHERE t.course IN ("
            +	"SELECT c FROM Course c JOIN c.students s "
            + 	"WHERE s.id=:studentId AND c.semester.year = :year AND c.semester.semesterType = :semesterType"
            + ")")
    public List<TimeTableItem> findByStudentAndSemester(int studentId, int year, Semester.SemesterType semesterType);


}
