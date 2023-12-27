package hu.webuni.student.web;

import hu.webuni.student.api.CourseControllerApi;
import hu.webuni.student.api.model.CourseDto;
import hu.webuni.student.api.model.HistoryDataCourseDto;
import hu.webuni.student.api.model.Pageable;
import hu.webuni.student.mapper.CourseMapper;
import hu.webuni.student.mapper.HistoryDataMapper;
import hu.webuni.student.model.Course;
import hu.webuni.student.model.HistoryData;
import hu.webuni.student.repository.CourseRepository;
import hu.webuni.student.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CourseController implements CourseControllerApi {

    private final NativeWebRequest nativeWebRequest;

    private final HistoryDataMapper historyDataMapper;
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
        Course course = courseRepository.save(courseMapper.dtoToCourse(courseDto));
        // szintén törölve áthelyezés miatt --> airports.put(airportDto.getId(), airportDto);
        // return airportDto; --->
        return ResponseEntity.ok(courseMapper.courseToDto(course));
    }

    @Override
    public ResponseEntity<Void> deleteCourse(Long id) {

        courseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Object> getAllCourse() {
        return ResponseEntity.ok(courseMapper.coursesToDtos(courseRepository.findAll()));
    }

    @Override
    public ResponseEntity<CourseDto> getCourseById(Long id) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(courseMapper.courseToDto(course));
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getCourseStatusByDate(Long id, LocalDateTime date) throws Throwable {
        List<HistoryData<Course>> courses = courseService.getCourseStatusByDateTime(id, date);


        List<HistoryDataCourseDto> courseDtosWithHistory =
                new ArrayList<>();

        courses.forEach(courseHistoryData ->
                courseDtosWithHistory.add(
                        historyDataMapper.courseHistoryDataToDto(courseHistoryData)
                        ));


//        return courseDtosWithHistory.stream().max(Comparator.comparing(courseDtoHistoryData -> courseDtoHistoryData.getDate())).stream().toList();

        return ResponseEntity.ok(courseDtosWithHistory);
    }

    @Override
    public ResponseEntity<CourseDto> getCourseStatusByDateOnlyValid(Long id, LocalDateTime date) throws Throwable {
        HistoryData<Course> course = courseService.getCourseStatusByDateOnlyValid(id, date);
        Course courseResult = course.getData();

        return ResponseEntity.ok(courseMapper.courseToDto(courseResult));
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getHistoryByDate(LocalDateTime date) {
        List<HistoryData<Course>> courses = courseService.getCourseHistoryByDate(date);


        List<HistoryDataCourseDto> courseDtosWithHistory =
                new ArrayList<>();

        courses.forEach(courseHistoryData ->
                courseDtosWithHistory.add(historyDataMapper.courseHistoryDataToDto(courseHistoryData))
                        /*
                        new HistoryData<>(
//                                courseMapper.courseSummaryToDto(courseHistoryData.getData()), //kapcsolatok nelkuli mappeles
                                courseMapper.courseToDto(courseHistoryData.getData()), //kapcsolatokkal mappeles, viszont lecsatolt allapotban lesznek, ennek a betolteset a service-ben kell kikenyszeriteni,
                                courseHistoryData.getRevType(),
                                courseHistoryData.getRevision(),
                                courseHistoryData.getDate()
                        ))*/
                        );


        return ResponseEntity.ok(courseDtosWithHistory);    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getHistoryById(Long id) {
        List<HistoryData<Course>> courses = courseService.getCourseHistory(id);


        List<HistoryDataCourseDto> courseDtosWithHistory =
                new ArrayList<>();

        courses.forEach(courseHistoryData ->
                courseDtosWithHistory.add(historyDataMapper.courseHistoryDataToDto(courseHistoryData)));
                /*
                        new HistoryData<>(
//                                courseMapper.courseSummaryToDto(courseHistoryData.getData()), //kapcsolatok nelkuli mappeles
                                courseMapper.courseToDto(courseHistoryData.getData()), //kapcsolatokkal mappeles, viszont lecsatolt allapotban lesznek, ennek a betolteset a service-ben kell kikenyszeriteni,
                                courseHistoryData.getRevType(),
                                courseHistoryData.getRevision(),
                                courseHistoryData.getDate()
                        )));

                 */

        return ResponseEntity.ok(courseDtosWithHistory);
    }

    @Override
    public ResponseEntity<CourseDto> modifyCourse(Long id, CourseDto courseDto) {
        Course course = courseMapper.dtoToCourse(courseDto);
        course.setId(id); // hogy tudjunk módosítani azonos iata-jút a uniqecheck ellenére
        try {
            CourseDto savedCourseDto = courseMapper.courseToDto(courseService.update(course));

            // LogEntryRepository.save(new LogEntry("Airport modified with id " + id)); -- service hozzáadva
            // logEntryService.createLog("Airport modified with id " + id); -inkább a service update legyen felelős érte, h a logot lementse
            // a service autowired-et is lehet így innét törölni, átvinni AirportService-be


            return ResponseEntity.ok(savedCourseDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
/*
    @Override
    public ResponseEntity<List<CourseDto>> search(Object predicate, Pageable pageable, Boolean full) {
        boolean isSummaryNeeded = full.isEmpty() || !full.get();
        Iterable<Course> result = isSummaryNeeded ?
                courseRepository.findAll(predicate, pageable) :
                courseService.searchCourses(predicate, pageable);
        //csak fullos esetben jon a select course es a select count melle meg a 2db custom lekerdezes is (student , teacher)
        System.out.println(result);
        if (isSummaryNeeded)
            return ResponseEntity.ok(courseMapper.courseSummariesToDtos(result));
        else
            return ResponseEntity.ok((List<CourseDto>) courseMapper.coursesToDtos(result));    }



 */
}
