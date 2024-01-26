package hu.webuni.student.security;

import hu.webuni.student.model.AppUser;
import hu.webuni.student.model.Student;
import hu.webuni.student.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacebookLoginService {

    private static final String GRAPH_API_BASE_URL = "https://graph.facebook.com/v13.0";

    private final UserRepository userRepository;

    @Getter
    @Setter
    public static class FacebookData {
        private String email;
        private long id;
    }


    @Transactional
    public UserDetails getUserDetailsForToken(String fbToken) {
        FacebookData fbData = getEmailOfFbUser(fbToken);
        AppUser appUser = findOrCreateUser(fbData);
        return AppUserDetailService.createUserDetails(appUser);

    }

    private FacebookData getEmailOfFbUser(String fbToken) { //HA INVALID A token, nem is ad vissza adatokat, ez egy validacio is egyben
        return WebClient.create(GRAPH_API_BASE_URL)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/me")
                        .queryParam("fields", "email,name")
                        .build()
                )
                .headers(headers -> headers.setBearerAuth(fbToken))
                .retrieve()
                .bodyToMono(FacebookData.class)
                .block(); // blocking until no answer
    }


    private AppUser findOrCreateUser(FacebookData facebookData) {

        String fbId = String.valueOf(facebookData.getId());
        Optional<AppUser> optionalExistingUser = userRepository.findByFacebookId(fbId);
        if(optionalExistingUser.isEmpty()) {
            Student newStudent = Student.builder()
                    .facebookId(fbId)
                    .username(facebookData.getEmail())
                    .password("dummy")
                    .build();

            return userRepository.save(newStudent);
        }

        return optionalExistingUser.get();
    }

}
