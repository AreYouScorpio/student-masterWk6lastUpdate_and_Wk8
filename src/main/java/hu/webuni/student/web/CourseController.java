package hu.webuni.student.web;

import hu.webuni.student.api.CourseControllerApi;
import hu.webuni.student.api.model.CourseDto;
import hu.webuni.student.api.model.HistoryDataCourseDto;
import hu.webuni.student.api.model.Pageable;
import hu.webuni.student.mapper.CourseMapper;
import hu.webuni.student.repository.CourseRepository;
import hu.webuni.student.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CourseController implements CourseControllerApi {

    private final NativeWebRequest nativeWebRequest;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseRepository courseRepository;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<CourseDto> createCourse(CourseDto courseDto) {
        return CourseControllerApi.super.createCourse(courseDto);
    }

    @Override
    public ResponseEntity<Void> deleteCourse(Long id) {
        return CourseControllerApi.super.deleteCourse(id);
    }

    @Override
    public ResponseEntity<Object> getAllCourse() {
        return CourseControllerApi.super.getAllCourse();
    }

    @Override
    public ResponseEntity<CourseDto> getCourseById(Long id) {
        return CourseControllerApi.super.getCourseById(id);
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getCourseStatusByDate(Long id, LocalDateTime date) {
        return CourseControllerApi.super.getCourseStatusByDate(id, date);
    }

    @Override
    public ResponseEntity<CourseDto> getCourseStatusByDateOnlyValid(Long id, LocalDateTime date) {
        return CourseControllerApi.super.getCourseStatusByDateOnlyValid(id, date);
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getHistoryByDate(LocalDateTime date) {
        return CourseControllerApi.super.getHistoryByDate(date);
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getHistoryById(Long id) {
        return CourseControllerApi.super.getHistoryById(id);
    }

    @Override
    public ResponseEntity<CourseDto> modifyCourse(Long id, CourseDto courseDto) {
        return CourseControllerApi.super.modifyCourse(id, courseDto);
    }

    @Override
    public ResponseEntity<List<CourseDto>> search(Object predicate, Pageable pageable, Boolean full) {
        return CourseControllerApi.super.search(predicate, pageable, full);
    }


}
