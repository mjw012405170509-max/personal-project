package com.main.personalprojectback.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebConfig {
    //이 설정은 민지원이 관리자 로그인 혹은 검증때 쓸 인터셉터를 구현하기 위해서 넣어놓은 로직입니다.
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(admin)
//                .addPathPatterns("/admin/**");
////                .excludePathPatterns(인터셉터 미적용 요청 패턴(필요 없을 수도 있음))
//        registry.addInterceptor(master)
//                .addPathPatterns("/master/**");
////                .excludePathPatterns(인터셉터 미적용 요청 패턴(필요 없을 수도 있음))
//        registry.addInterceptor(users)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/users/login","/users/join","/master/**","/admin/**");
//
////    }
}
