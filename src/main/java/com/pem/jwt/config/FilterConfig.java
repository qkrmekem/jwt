package com.pem.jwt.config;

import com.pem.jwt.filter.MyFilter1;
import com.pem.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");
        // 필터의 우선순위를 정하는 메소드
        // 숫자가 낮을 수록 우선순위는 높다.
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        // 필터의 우선순위를 정하는 메소드
        // 숫자가 낮을 수록 우선순위는 높다.
        bean.setOrder(1);
        return bean;
    }
}
