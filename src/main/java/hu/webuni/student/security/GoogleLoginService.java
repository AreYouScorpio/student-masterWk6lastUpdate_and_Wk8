package hu.webuni.student.security;

import hu.webuni.student.model.AppUser;
import hu.webuni.student.model.Student;
import hu.webuni.student.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private static final String GOOGLE_BASE_URI = "https://oauth2.googleapis.com";

    private final UserRepository userRepository;

    @Value("${university.google-client-id}") //springes @Value, nem lombokos
    private String googleClientId;
    //You can provide a default value in case the property is not found. For example: @Value("${university.google-client-id:default-value}").

    @Getter
    @Setter
    public static class GoogleData {
        private String sub;
        private String email;

        private String aud;

        private String iss;
        private String azp;
        private String email_verified;
        private String at_hash;
        private String name;
        private String picture;
        private String given_name;
        private String family_name;
        private String locale;
        private String iat;
        private String exp;
        private String jti;
        private String alg;
        private String kid;
        private String typ;
    }

    @Transactional
    public UserDetails getUserDetailsForToken(String googleToken) {

        GoogleData googleData = getGoogleDataForToken(googleToken);
        if(!this.googleClientId.equals(googleData.getAud()))
            throw new BadCredentialsException("The aud claim of the google id token does not match the client id");

        AppUser appUser = findOrCreateUser(googleData);
        return AppUserDetailService.createUserDetails(appUser);
    }

    private AppUser findOrCreateUser(GoogleData googleData) {
        String googleId = googleData.getSub();
        Optional<AppUser> optionalExistingUser = userRepository.findByGoogleId(googleId);
        if(optionalExistingUser.isEmpty()) {

            return userRepository.save(Student.builder()
                    .googleId(googleId)
                    .username(googleData.getEmail())
                    .password("dummy")
                    .courses(Collections.emptySet())
                    .build());
        }

        return optionalExistingUser.get();
    }

    private GoogleData getGoogleDataForToken(String googleToken) {

        return WebClient.create(GOOGLE_BASE_URI)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tokeninfo")
                        .queryParam("id_token", googleToken)
                        .build())
                .retrieve()
                .bodyToMono(GoogleData.class)
                .block();
    }

}
