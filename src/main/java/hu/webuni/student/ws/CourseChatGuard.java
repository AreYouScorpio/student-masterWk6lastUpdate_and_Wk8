package hu.webuni.student.ws;

import hu.webuni.student.security.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CourseChatGuard {

    public boolean checkCourseId(Authentication authentication, int courseId) {
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return userInfo.getCourseIds().contains(courseId);
    } // ekkor engedjuk a feliratkozast

}
