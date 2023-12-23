package hu.webuni.student.web;

import com.querydsl.core.types.Predicate;
import hu.webuni.student.api.model.CourseDto;
import hu.webuni.student.mapper.CourseMapper;
import hu.webuni.student.model.Course;
import hu.webuni.student.model.HistoryData;
import hu.webuni.student.repository.CourseRepository;
import hu.webuni.student.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    //https://mapstruct.org/ minták !!! és pom.xml --- https://mapstruct.org/documentation/installation/

    @Autowired
    CourseService courseService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseRepository courseRepository;

    //@Autowired
    //LogEntryService logEntryService;

    @GetMapping
    public Iterable<CourseDto> getAllCourse() {
        return courseMapper.coursesToDtos(courseRepository.findAll());
    }


    @GetMapping("/{id}")
    public CourseDto getCourseById(@PathVariable long id) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // deleted after mapper ---> AirportDto airportDto = airports.get(id);
//        if (airportDto!=null)
//            return ResponseEntity.ok(airportDto);
//        else
//        return ResponseEntity.notFound().build();
       /* ehelyett is orElseThrow és a return marad
        if (airport != null)
            return airportMapper.airportToDto(airport);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        */
        return courseMapper.courseToDto(course);

    }


    @PostMapping
    public CourseDto createCourse(@RequestBody @Valid CourseDto courseDto /*, BindingResult errors */) {
        //if (errors.hasErrors()) throw new ...


        // áthelyezve mapper bevezetésével a service-be:
        // checkUniqueIata(airportDto.getIata());

        Course course = courseRepository.save(courseMapper.dtoToCourse(courseDto));
        // szintén törölve áthelyezés miatt --> airports.put(airportDto.getId(), airportDto);
        // return airportDto; --->
        return courseMapper.courseToDto(course);
    }


    // Teacher`s solution added Wk1:

    // Wk2 cache added
    @Cacheable("pagedCourses")
    @GetMapping("/searchNew")
    public List<CourseDto> search(@QuerydslPredicate(root = Course.class) Predicate predicate, @RequestParam Optional<Boolean> full, @SortDefault("id") Pageable pageable) {
        //id szt legyen default rendezes, h mar az elso page is rendezetten jojjon, ne legyen gond kesobb
        //Iterable<Course> result = courseRepository.findAll(predicate);
        boolean isSummaryNeeded = full.isEmpty() || !full.get();
        Iterable<Course> result = isSummaryNeeded ?
                courseRepository.findAll(predicate, pageable) :
                courseService.searchCourses(predicate, pageable);
        //csak fullos esetben jon a select course es a select count melle meg a 2db custom lekerdezes is (student , teacher)
        System.out.println(result);
        if (isSummaryNeeded)
            return courseMapper.courseSummariesToDtos(result);
        else
            return (List<CourseDto>) courseMapper.coursesToDtos(result);
    }


    @GetMapping("/{id}/history")
    public List<HistoryData<CourseDto>> getHistoryById(@PathVariable long id) {


        List<HistoryData<Course>> courses = courseService.getCourseHistory(id);


        List<HistoryData<CourseDto>> courseDtosWithHistory =
                new ArrayList<>();

        courses.forEach(courseHistoryData ->
                courseDtosWithHistory.add(
                        new HistoryData<>(
//                                courseMapper.courseSummaryToDto(courseHistoryData.getData()), //kapcsolatok nelkuli mappeles
                                courseMapper.courseToDto(courseHistoryData.getData()), //kapcsolatokkal mappeles, viszont lecsatolt allapotban lesznek, ennek a betolteset a service-ben kell kikenyszeriteni,
                                courseHistoryData.getRevType(),
                                courseHistoryData.getRevision(),
                                courseHistoryData.getDate()
                        )));

        return courseDtosWithHistory;


    }


    @GetMapping("/{date}/historyByDate")
    public List<HistoryData<CourseDto>> getHistoryByDate(@PathVariable LocalDateTime date) {


        List<HistoryData<Course>> courses = courseService.getCourseHistoryByDate(date);


        List<HistoryData<CourseDto>> courseDtosWithHistory =
                new ArrayList<>();

        courses.forEach(courseHistoryData ->
                courseDtosWithHistory.add(
                        new HistoryData<>(
//                                courseMapper.courseSummaryToDto(courseHistoryData.getData()), //kapcsolatok nelkuli mappeles
                                courseMapper.courseToDto(courseHistoryData.getData()), //kapcsolatokkal mappeles, viszont lecsatolt allapotban lesznek, ennek a betolteset a service-ben kell kikenyszeriteni,
                                courseHistoryData.getRevType(),
                                courseHistoryData.getRevision(),
                                courseHistoryData.getDate()
                        )));

        return courseDtosWithHistory;


    }

    @GetMapping("/{id}/{date}/statusByDate")
    public List<HistoryData<CourseDto>> getCourseStatusByDate(@PathVariable long id, @PathVariable LocalDateTime date) throws Throwable {


        List<HistoryData<Course>> courses = courseService.getCourseStatusByDateTime(id, date);


        List<HistoryData<CourseDto>> courseDtosWithHistory =
                new ArrayList<>();

        courses.forEach(courseHistoryData ->
                courseDtosWithHistory.add(
                        new HistoryData<>(
//                                courseMapper.courseSummaryToDto(courseHistoryData.getData()), //kapcsolatok nelkuli mappeles
                                courseMapper.courseToDto(courseHistoryData.getData()), //kapcsolatokkal mappeles, viszont lecsatolt allapotban lesznek, ennek a betolteset a service-ben kell kikenyszeriteni,
                                courseHistoryData.getRevType(),
                                courseHistoryData.getRevision(),
                                courseHistoryData.getDate()
                        )));


//        return courseDtosWithHistory.stream().max(Comparator.comparing(courseDtoHistoryData -> courseDtoHistoryData.getDate())).stream().toList();

        return courseDtosWithHistory;

//        return courseMapper.courseSummariesToDtos(courses);

    }


    @GetMapping("/{id}/{date}/statusByDateOnlyValid")
    public CourseDto getCourseStatusByDateOnlyValid(@PathVariable long id, @PathVariable LocalDateTime date) throws Throwable {


        HistoryData<Course> course = courseService.getCourseStatusByDateOnlyValid(id, date);
        Course courseResult = course.getData();

        return courseMapper.courseToDto(courseResult);


    }


    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable long id) {
        courseService.delete(id);
    }


    /*

    @PutMapping("/{id}")
    public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id,
                                                    @RequestBody AirportDto airportDto) {
        if (!airports.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }

        checkUniqueIata(airportDto.getIata());
        airportDto.setId(id);
        airports.put(id, airportDto);
        return ResponseEntity.ok(airportDto);
    }
new PutMapping after MapStruct added:
---->

    */

    /* saját mego:

    @PutMapping("/{id}")
    public AirportDto modifyAirport(@PathVariable long id,
                                                    @RequestBody @Valid AirportDto airportDto) {

        Airport airport = airportService.findById(id);


        if (airport != null)
            airportService.update(id, airportMapper.dtoToAirport(airportDto));
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);


        return airportMapper.airportToDto(airport);


    }



     */


    // ---> ehelyett tanári megoldás, de enyém is működött ---->


    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> modifyCourse(@PathVariable long id,
                                                  @RequestBody CourseDto courseDto) {

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


    /* my old solution

    @PostMapping("/search")
    public List<CourseDto> searchCourses(@RequestBody CourseDto example) {


        return courseMapper.coursesToDtos(courseService.findCoursesByExample(courseMapper.dtoToCourse(example)));
    }


     */


}
