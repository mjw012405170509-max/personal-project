package com.main.personalprojectback.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    /**
     * 비밀번호 암호화 객체 등록
     * 회원가입 시 encode()
     * 로그인 시 matches()로 비교
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    /**
     * Spring Security 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // REST API는 보통 csrf 끔
                .csrf(csrf -> csrf.disable())
//                .cors(cors -> {})
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 스프링 기본 로그인 페이지 사용 안함
                .formLogin(form -> form.disable())

                // 브라우저 팝업 인증 비활성화
                .httpBasic(basic -> basic.disable())

                // URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> //auth
                        auth.anyRequest().permitAll()
                        // 회원가입, 로그인은 누구나 접근 가능
//                        .requestMatchers(
//                                "/file/test"
////                                "/auth/signup",
////                                "/auth/login"
//                        )
//                        .permitAll()
//
//                        // admin으로 시작하는 주소는 ADMIN만
//                        .requestMatchers("/admin/**")
//                        .hasRole("ADMIN")
//
//                        // 그 외는 로그인 필요
//                        .anyRequest()
//                        .authenticated()
                );

        return http.build();

    }
    //쿠키 관련 설정들을 몰아넣은 구간
    /**
     * CORS 설정
     *
     * React와 Spring Boot는 서로 다른 포트에서 실행된다.
     *
     * 예시)
     * React  : localhost:5173
     * Spring : localhost:9999
     *
     * 브라우저는 포트가 다르면 다른 서버라고 판단한다.
     *
     * 따라서 React가 Spring API를 호출하려고 하면
     * 브라우저가 기본적으로 요청을 차단한다.
     *
     * 이 설정은
     * "localhost:5173에서 오는 요청은 허용해도 된다"
     * 라고 브라우저에게 알려주기 위한 설정이다.
     *
     * 또한 JWT를 HttpOnly Cookie로 저장할 예정이므로
     * 쿠키를 함께 주고받을 수 있도록
     * allowCredentials(true)를 사용한다.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration
                = new CorsConfiguration();

        // 프론트 주소 허용
        // React 개발 서버 주소 허용
        // 이 주소에서 오는 요청만 백엔드 접근 가능
        configuration.setAllowedOrigins(
                List.of("http://localhost:5173","http://localhost:5174")

        );

        // 허용 메서드
        // 허용할 HTTP 요청 방식
        //
        // GET    : 조회
        // POST   : 등록
        // PUT    : 수정
        // DELETE : 삭제
        // OPTIONS: 브라우저의 사전 확인 요청
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );
        // 요청 Header 허용
        //
        // Content-Type
        // Authorization
        // Cookie
        // 등 모든 Header 허용
        // 모든 헤더 허용
        configuration.setAllowedHeaders(
                List.of("*")
        );

        // ⭐ 쿠키 허용
        // 쿠키 사용 허용
        //
        // React에서
        // withCredentials: true
        //
        // Spring에서
        // allowCredentials(true)
        //
        // 둘 다 설정되어 있어야
        // 브라우저가 쿠키를 서버에 보낸다.
        //
        // JWT를 쿠키에 저장할 예정이므로 필수 설정(핵심 설정)
        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source
                = new UrlBasedCorsConfigurationSource();
        // 위에서 만든 CORS 설정을
        // 모든 URL에 적용
        //
        // 예)
        // /user/login
        // /user/signup
        // /document/list
        // /admin/*
        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }


}
