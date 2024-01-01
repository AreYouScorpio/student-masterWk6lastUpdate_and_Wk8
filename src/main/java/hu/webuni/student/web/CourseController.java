package hu.webuni.student.web;

import com.querydsl.core.types.Predicate;
import hu.webuni.student.api.CourseControllerApi;
import hu.webuni.student.api.model.CourseDto;
import hu.webuni.student.api.model.HistoryDataCourseDto;
import hu.webuni.student.mapper.CourseMapper;
import hu.webuni.student.mapper.HistoryDataMapper;
import hu.webuni.student.model.Course;
import hu.webuni.student.model.HistoryData;
import hu.webuni.student.repository.CourseRepository;
import hu.webuni.student.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

    private final PageableHandlerMethodArgumentResolver pageableResolver;

    private final QuerydslPredicateArgumentResolver predicateResolver;
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


        return ResponseEntity.ok(courseDtosWithHistory);
    }

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

    public void configPageable(@SortDefault("id") Pageable pageable) { //ahogy itt annotaljuk, az kerul a methodparameterbe a search-ben, de ha mashol maskepp akarjuk, felvehetunk ujabb metodusokat

    }

    public void configurePredicate(@QuerydslPredicate(root = Course.class) Predicate predicate) { //ahogy itt annotaljuk, az kerul a methodparameterbe a search-ben, de ha mashol maskepp akarjuk, felvehetunk ujabb metodusokat

    }


    @Override
    public ResponseEntity<List<CourseDto>> search(Boolean full, Integer page, Integer size, List<String> sort, Long id) {

        //id szt legyen default rendezes, h mar az elso page is rendezetten jojjon, ne legyen gond kesobb
        //Iterable<Course> result = courseRepository.findAll(predicate);
        //boolean isSummaryNeeded = full.isEmpty() || !full.get();
        boolean isSummaryNeeded = full == null ? false : full;
        Pageable pageable = createPageable("configPageable");

        Predicate predicate = createPredicate("configurePredicate");

        Iterable<Course> result = isSummaryNeeded ?
                courseRepository.findAll(predicate, pageable) :
                courseService.searchCourses(predicate, pageable);
        //csak fullos esetben jon a select course es a select count melle meg a 2db custom lekerdezes is (student , teacher)
        System.out.println(result);
        List<CourseDto> resultList = (List<CourseDto>) courseMapper.coursesToDtos(result);
        if (isSummaryNeeded)
            return ResponseEntity.ok(courseMapper.courseSummariesToDtos(result));
        else
            return ResponseEntity.ok(resultList);
    }

    private Predicate createPredicate(String configMethodname) {
        //return predicateResolver.resolveArgument()-tel gyartatjuk le
        try {
            Method method = this.getClass().getMethod(configMethodname, Predicate.class); // a metodus neve, a metodus argumentumlistaja
            MethodParameter methodParameter = new MethodParameter(method, 0); // csinalunk egy method local vart is
            // --> azt irja le, h a pageable tipusu metodusargumentum melyik controllermetodus hanyadik argumentuma
            // megnezi azt is, van-e masik annotacioja .. pl mi is ratettuk a @SortDefault("id")-t
            // nekunk nincs sehol egy pageable bemeno parameterunk -> ha nincs, akk csinalunk egy ilyen metodust -> public void configPageable
            // mindig behazudhatjuk, hanyadik argumentum
            ModelAndViewContainer mavContainer = null; // no need for getting pageable
            WebDataBinderFactory binderFactory = null; // no need for getting pageable
            //these 3 resolveArgument needed:
            return (Predicate) predicateResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private Pageable createPageable(String pageableConfigureMethodName) {
        try {
            Method method = this.getClass().getMethod(pageableConfigureMethodName, Pageable.class); // a metodus neve, a metodus argumentumlistaja
            MethodParameter methodParameter = new MethodParameter(method, 0); // csinalunk egy method local vart is
            // --> azt irja le, h a pageable tipusu metodusargumentum melyik controllermetodus hanyadik argumentuma
            // megnezi azt is, van-e masik annotacioja .. pl mi is ratettuk a @SortDefault("id")-t
            // nekunk nincs sehol egy pageable bemeno parameterunk -> ha nincs, akk csinalunk egy ilyen metodust -> public void configPageable
            // mindig behazudhatjuk, hanyadik argumentum
            ModelAndViewContainer mavContainer = null; // no need for getting pageable
            WebDataBinderFactory binderFactory = null; // no need for getting pageable
            //these 3 resolveArgument needed:
            Pageable pageable = pageableResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);
            return pageable;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
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
