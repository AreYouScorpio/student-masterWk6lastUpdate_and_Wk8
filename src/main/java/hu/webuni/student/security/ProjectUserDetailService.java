package hu.webuni.student.security;

import com.ggp.GGP_ATS_Project.repository.UserRepository;
import hu.webuni.student.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProjectUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    // this will prepare user object for spring security from DB:
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(username,
                user.getPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }
}
