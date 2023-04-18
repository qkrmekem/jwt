package com.pem.jwt.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");
        // 여기서 response를 이용해 응답을 보내버리면 프로세스가 끝나버리기 때문에
        // 프로세스를 진행하기 위해 다시 필터에 요청과 응답을 넘겨줘야 한다.
        chain.doFilter(request, response);
    }
}
