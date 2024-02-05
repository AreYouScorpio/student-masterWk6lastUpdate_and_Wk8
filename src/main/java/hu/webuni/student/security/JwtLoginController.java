package hu.webuni.student.security;

import hu.webuni.student.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtLoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;


    private final FacebookLoginService facebookLoginService;
    private final GoogleLoginService googleLoginService;

    @PostMapping("/api/login")
    public String login(@RequestBody LoginDto loginDto) {


        UserDetails userDetails = null;
        //check whether the user wants fb login (is fbToken?)
        String fbToken = loginDto.getFbToken();
        String googleToken = loginDto.getGoogleToken();
        if (ObjectUtils.isEmpty(fbToken)) {
            if (ObjectUtils.isEmpty(googleToken)) {
                //ha mindegyik ures, sima authentikacio
/* old
            Authentication authentication =
                    authenticationManager
                            .authenticate(
                                    new UsernamePasswordAuthenticationToken(
                                            loginDto.getUsername(),
                                            loginDto.getPassword()));

 */

            userDetails = (UserDetails)
                    authenticationManager
                            .authenticate(
                                    new UsernamePasswordAuthenticationToken(
                                            loginDto.getUsername(),
                                            loginDto.getPassword())).getPrincipal();} else {

                userDetails = googleLoginService.getUserDetailsForToken(googleToken);
            }

        } else {
            userDetails = facebookLoginService.getUserDetailsForToken(fbToken);

        }


        // return "\"" + jwtService.createJwtToken((UserDetails)authentication.getPrincipal()) + "\"";
        return "\"" + jwtService.createJwtToken(userDetails) + "\"";

    }

}
