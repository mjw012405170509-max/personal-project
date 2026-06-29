package com.main.personalprojectback.controller;

import com.main.personalprojectback.DTO.LoginDto;
import com.main.personalprojectback.DTO.LoginReturnDto;
import com.main.personalprojectback.DTO.UserDto;
import com.main.personalprojectback.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService service;
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserDto user){
//    }
    @Value("${jwt.expire-hour}")
    private int expireHour;


    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserDto user){//@Valid는 userDto의 @Pattern어노테이션이 터지면 그대로 오류 반환
        int result = service.signIn(user);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/idCheck")
    public ResponseEntity<?> idCheck(@RequestParam(value = "userId") String userId){
        int result = service.idCheck(userId);
        if(result==0){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto user, HttpServletResponse servlet){
        LoginReturnDto result = service.login(user);
        if(result.isLoginStatus()){

            Cookie cookie = new Cookie(
                    "accessToken",
                    result.getToken()
            );

            // JavaScript 접근 차단
            cookie.setHttpOnly(true);

            // 개발환경에서는 false
            // HTTPS 운영환경에서는 true
            cookie.setSecure(false);
            // 모든 URL에서 사용
            cookie.setPath("/");
            // 1시간 유지
            cookie.setMaxAge(expireHour * 60 * 60);//application의 시간값이 바뀌면 자동으로 바뀌게 설정

            servlet.addCookie(cookie);
        }
//        System.out.println("tlqkf");
        return ResponseEntity.ok(result);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        //쿠키 삭제(정확히는 덮어쓰기를 해서 무효화)
        Cookie cookie = new Cookie("accessToken","");

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        //쿠키의 시간을 0으로 설정해서 무효화
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        System.out.println("로그아웃요청들어옴");
        return ResponseEntity.ok(true);
    }

}
