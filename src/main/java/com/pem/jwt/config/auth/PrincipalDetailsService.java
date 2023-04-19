package com.pem.jwt.config.auth;

import com.pem.jwt.model.User;
import com.pem.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login => formLogin()을 disable로 설정해뒀기 때문에 작동을 안함
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername");
        User user = userRepository.findByUsername(username);
        System.out.println("user = " + user);
        return new PrincipalDetails(user);
    }
}
