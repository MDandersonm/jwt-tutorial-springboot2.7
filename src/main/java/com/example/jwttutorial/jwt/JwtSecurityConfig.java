package com.example.jwttutorial.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//토큰provider와 jwt필터를 securityConfig에 적용할때 사용할 config파일이다.
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private TokenProvider tokenProvider;
    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) {
        //jwt filter를 시큐리티로직에 필터로 등록하는 역할을 한다.
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(
                customFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
