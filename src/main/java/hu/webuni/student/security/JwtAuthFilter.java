package hu.webuni.student.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader!=null && authHeader.startsWith(BEARER)) {
            String jwtToken = authHeader.substring(BEARER.length());
            UserDetails principal = jwtService.parseJwt(jwtToken);

            // old: Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()); // a második lenne a jelszó vagy hitelesítő, most null
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()); // a második lenne a jelszó vagy hitelesítő, most null
            // ha érdekel, milyen IP-ről jön ez a user v ha lenne session ID-ja, akk mi az, akk a köv sor.. plusz előzőben Authenticationt átalakítani
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response); // továbbengedjük a kérést és a választ
    }

    public static UsernamePasswordAuthenticationToken createUserDetailsFromAuthHeader(String authHeader, JwtService jwtService) {
        if(authHeader != null && authHeader.startsWith(BEARER)) {
            String jwtToken = authHeader.substring(BEARER.length());
            UserDetails principal = jwtService.parseJwt(jwtToken);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            return authentication;
        }
        return null;
    }
}
