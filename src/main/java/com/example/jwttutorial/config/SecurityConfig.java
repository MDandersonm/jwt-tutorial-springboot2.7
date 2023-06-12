package com.example.jwttutorial.config;

import com.example.jwttutorial.jwt.JwtAccessDeniedHandler;
import com.example.jwttutorial.jwt.JwtAuthenticationEntryPoint;
import com.example.jwttutorial.jwt.JwtSecurityConfig;
import com.example.jwttutorial.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity  // 기본적인 웹보안을 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true)//@PreAuthorize라는 어노테이션을 메소드단위로 사용하기위해서
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
//    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    public SecurityConfig(
            TokenProvider tokenProvider,
//            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
//        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web){
        web
                .ignoring()
                .antMatchers("/h2-console/**","/favicon.ico");
        // Spring Security에서 무시되어, 이러한 경로로의 모든 요청은 보안 필터링을 통과하지 않습니다.
        //, WebSecurity는 전체적인 보안을 설정하며, HttpSecurity는 HTTP 요청에 대한 보안 세부 사항을 설정합니다.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//토큰방식을 사용하기떄문에 disable해줌
                .exceptionHandling()//익셉션을 핸들링할때 우리가 만들었던 클래스를 적용
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //h2console을 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()


                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션은 사용하지않기때문에 세션설정을 stateless로 설정


                .and()
                .authorizeRequests()//: http서블릿리퀘스트를 사용하는 요청들에대한 접근제한을 설정하겠다는 의미
                .antMatchers("/api/hello").permitAll()//인증없이 접근허용
                .antMatchers("/api/authenticate").permitAll()//토큰을 받기위한 로그인 api(토큰없는상태에서 요청되서 열어둬야함)
                .antMatchers("/api/signup").permitAll()//회원가입 api (토큰없는상태에서 요청)
                .anyRequest().authenticated()//나머지요청들은 모두 인증받아야한다는 의미.
//                .antMatchers("/api/hello").authenticated()  // "/api/hello" 엔드포인트에 인증 요구
//                .anyRequest().permitAll()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));// jwtfilter를  addfilterBefore로 등록했던 jwtsecurityconfig클래스도 적용
                //.httpBasic();
    }


}
