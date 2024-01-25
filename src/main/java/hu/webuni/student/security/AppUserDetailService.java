package hu.webuni.student.security;

import hu.webuni.student.model.AppUser;
import hu.webuni.student.model.Course;
import hu.webuni.student.model.Student;
import hu.webuni.student.model.Teacher;
import hu.webuni.student.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    // This will prepare a user object for Spring Security from the database
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        /* old
        return new User(username,
                appUser.getPassword(),
                appUser.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));

         */
        //new

        Set<Course> courses = null;
        if (appUser instanceof Teacher teacher) //a cast-olt valtozonak nevet is adhatok egybol >java17(?)
        {
            courses = teacher.getCourses();
        } else if (appUser instanceof Student student) {
            courses = student.getCourses();
        }

        return new UserInfo(
                username,
                appUser.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(appUser.getUserType().toString())),
                courses == null ? null : courses.stream()
                        .map(course -> (int) (long) course.getId())
                        .collect(Collectors.toList()));
    }
}
