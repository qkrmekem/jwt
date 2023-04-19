package com.pem.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pem.jwt.config.auth.PrincipalDetails;
import com.pem.jwt.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

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
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("user = " + user);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService의 loadUserByUsername() 메서드가 실행된 후
            // 정상처리 되면 authentication이 리턴 됨
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            // Authentication 객체가 session영역에 저장됨
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            // 아래 출력문이 제대로 찍혔다는 것은 로그인이 되었다는 뜻.
            System.out.println("로그인 완료 됨 = " + principalDetails.getUser().getUsername());
            // authentication 객체가 session영역에 저장을 해야하고 그 방법이 authencitaion을 리턴해주면 된다.
            // 리턴해주는 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는 것이고
            // 이게 아니라면 jwt를 사용하면서 세션을 생성할 이유가 없다.
            return authentication;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    //  attemptAuthentication실행 후 인증이 정상적으로 되었다면 successfulAuthentication가 실행된다.
    // 여기서 JWT를 생성하여 request한 사용자에게 토큰을 반환해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication이 실행됨 => 인증이 완료됨");

        // 추가
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA방식은 아니구 Hash암호방식
        String jwtToken = JWT.create()
                .withSubject("pem토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("pem"));

        response.addHeader("Authorization", "Bearer "+jwtToken);

        System.out.println("토큰 생성 후 헤더에 첨부 완료");
    }
}
