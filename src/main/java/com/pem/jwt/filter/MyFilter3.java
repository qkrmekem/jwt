package com.pem.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;


        // http메서드를 확인하는 메서드
//        System.out.println(req.getMethod());
        // 토큰 = pem 이걸 만들어줘야함.
        // id, pw가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해준다,
        // 요청할 때마다 header의 Authorization의 value로 토큰을 가져올 것이고
        // 이 토큰이 내가 만든 토큰이 맞는지만 검증하면 된다.(RSA, HS256)
        if (req.getMethod().equals("POST")) {
            System.out.println("POST요청됨!!");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth = " + headerAuth);

            if (headerAuth.equals("pem")) {
                chain.doFilter(req, res);
            } else {
                PrintWriter writer = res.getWriter();
                writer.println("인증안됨");
            }
        }

        System.out.println("필터3");
        // 여기서 response를 이용해 응답을 보내버리면 프로세스가 끝나버리기 때문에
        // 프로세스를 진행하기 위해 다시 필터에 요청과 응답을 넘겨줘야 한다.
        chain.doFilter(request, response);
    }
}
