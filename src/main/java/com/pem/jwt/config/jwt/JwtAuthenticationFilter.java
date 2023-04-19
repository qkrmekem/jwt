package com.pem.jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// '/login'요청을 해서 username, password를 POST로 전송하면
// UsernamePasswordAuthenticationFilter가 동작함
// 그런데 현재는 '/login'이 작동하지 않기 때문에 필터를 새로 등록해줘야함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // '/login'요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중");

        // 1. username, password를 받아서

        // 2. 정상인지 로그인을 시도해본다. authenticationManager로 로그인을 시도하면
        // PrincipalDetailsService의 loadUserByUsername()가 실행됨

        // 3. PrincipalDetails를 세션에 담고 (권한 관리를 위해서)

        // 4. JWT토큰을 만들어서 응답해주면 됨
        return super.attemptAuthentication(request, response);
    }
}