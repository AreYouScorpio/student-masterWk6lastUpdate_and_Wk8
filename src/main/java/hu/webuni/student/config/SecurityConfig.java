package hu.webuni.student.config;

import hu.webuni.student.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

/*
    //USERS
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authManagerBuilder.authenticationProvider(authenticationProvider()).build();
    }


 */
    //RULES
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
//			.httpBasic()
//			.and()
                    .csrf(
                            csrf -> csrf.disable()
                    )
                    //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    //.and()
                    //.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/oauth2/**").permitAll()
                            .requestMatchers("/fbLoginSuccess").permitAll()
                            .requestMatchers("/api/login/**").permitAll()
                            .requestMatchers("/api/stomp/**").permitAll()
                            .requestMatchers("/api/v3/**").permitAll()
                            //.requestMatchers("/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/courses/**").hasAuthority("TEACHER")
                            .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAuthority("TEACHER")
                            .anyRequest().authenticated()
                    )
                    //.oauth2Login(oAuth2Login -> oAuth2Login
                    //        .defaultSuccessUrl("/fbLoginSuccess", true)
                    //)
            ;

            http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }
/*
        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
            daoAuthenticationProvider.setUserDetailsService(userDetailsService);
            return daoAuthenticationProvider;
        }



 */

}


// update korabbirol:


//Sziasztok, a hatodik heti tananyag csak holnaptól érhető el, de beírom már most a kapcoslódó Spring Boot 3-as változásokat:
//https://github.com/imre-gabor/webuni-spring-kh/pull/10/files
//
//Már a 2.7-es Spring Boot starter parent által behúzott Spring Security 5.7 deprecateddé tette a széles körben használt WebSecurityConfigurerAdaptert, majd a későbbi verziókból teljesen törölték is. Ezen kívül néhány más változás is történt, mire eljutottunk a Spring Boot 3-ban lévő Spring Security 6-ig. A pom-ban a legtöbb security-s függőségünket a parent menedzseli, így ezeken nem kellett verziót váltani. Kivétel ez alól a com.auth0 java-jwt, ebből a legfrissebbre álltam át.
//
//Ezek a főbb módosítások a SecurityConfig-ban még a websocket security-t és a Facebook logint megelőzően az alap JWT-s loginhoz:
//
//1. Az @EnableGlobalMethodSecurity(prePostEnabled = true) helyett @EnableMethodSecurity annotáció kell
//
//2. A megszüntetett WebSecurityConfigurerAdapter ősből örökölt void configure(AuthenticationManagerBuilder auth) metódus felüldefiniálása helyett egy
//
//@Bean
//public AuthenticationManager authManager(HttpSecurity http)
//
//metódusban kell legyártanunk az AuthenticationManager-t, ami az autentikáció módját határozza meg. Mivel ez a metódus már eleve beanként publikálja az AuthenticationManager-t, elhagyható ez a metódus (illetve el is kell hagyni):
//
//@Override
//@Bean
//public AuthenticationManager authenticationManagerBean() throws Exception {
//      return super.authenticationManagerBean();
//}
//
//3.  Az ősből örökölt void configure(HttpSecurity http) metódus helyett egy
//@Bean
//public SecurityFilterChain filterChain(HttpSecurity http)
//metódusban kell a CSRF és session használatra, valamint az autorizációra vonatkozó szabályokat beállítanunk. A beállítás kicsit más stílusban is történik: korábban argumentumok nélkül hívható volt pl. a csrf() és authorizeRequests(), majd ezek után jöttek az adott területhez tartozó konfiguráló metódusok, pl.
//.csrf().disable()
//vagy
//.authorizeRequests()
//.antMatchers("/api/login/**").permitAll()
//
//Most minden konfig metódus bemenő argumentumként kap egy lambdát, amiben a paraméterül kapott configurer segítségével kell megtenni a beállításokat, pl.
//.csrf(csrf ->
//	csrf.disable()
//)
//
//A Facebook-os loginhoz a felpusholt facebook client-id és client-secret már nem érvényes, de nem pusholtam újat, mert amúgysem javasolt ezeket git repoban publikálni. Ha egy saját Facebook appot konfigoltok, arra kell majd figyelni, hogy azóta kötelező a Privacy Policy URL és User Data Deletion URL megadása. Ide bármilyen publikusan elérhető URL-t megadhattok egyelőre.
//Kódban csak egy dolog változott, a fent írt stílusra váltottam át az oauth2Login konfigjánál:
//https://github.com/imre-gabor/webuni-spring-kh/pull/10/files#diff-28be89f76b66da89ea872601f9825fb8fb07aceaa17ada0d172df3d633a0e5c7R63
//
//A web socket security új stílusú konfigjáról itt lehet olvasni:
//https://docs.spring.io/spring-security/reference/6.0/servlet/integrations/websocket.html
//Az AbstractSecurityWebSocketMessageBrokerConfigurer deprecated, helyette az @EnableWebSocketSecurity annotáció használható, és az általam a videón mutatott
//	@Override
//	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//		messages.simpSubscribeDestMatchers("/topic/delay/*").hasAuthority("admin");
//	}
//helyett így kellene az autorizációt beállítani:
//    @Bean
//    AuthorizationManager messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
//        messages
//                .simpSubscribeDestMatchers("/topic/delay/*").hasAuthority("admin");
//        return messages.build();
//    }
//
//Mégsem álltam át erre a megoldásra, mert az @EnableWebSocketSecurity  egyelőre nem támogatja a CSRF kikapcsolását. Azt egyelőre csak XML konfiggal, vagy pont a videón mutatott régebbi API-val lehet megoldani.
//https://docs.spring.io/spring-security/reference/6.1.1/servlet/integrations/websocket.html#websocket-sameorigin-disable
//
//Írjatok nyugodtan, ha kérdés merülne fel!