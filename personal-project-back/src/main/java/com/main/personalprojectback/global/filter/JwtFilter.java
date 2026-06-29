package com.main.personalprojectback.global.filter;

import com.main.personalprojectback.DTO.LoginInfoDto;
import com.main.personalprojectback.global.security.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;

/**
 * JWT 인증 필터
 * 인터셉터보다 먼저 실행됨
 * 역할
 * 1. 요청에 포함된 쿠키 조회
 * 2. accessToken 추출
 * 3. JWT 검증 (추후 구현)
 * 4. 인증 성공 시 요청 통과
 * 5. 인증 실패 시 차단
 */

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwt;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException{

        // 브라우저가 현재 요청과 함께 보낸 모든 쿠키를 가져온다.
        // 로그인에 성공했다면 accessToken 쿠키도 여기에 포함되어 있다.
        Cookie[] cookies = request.getCookies();

        // accessToken 값을 저장할 변수
        String token = null;
        // 쿠키가 존재하는 경우에만 검사
        if(cookies != null){
            // 모든 쿠키를 하나씩 확인(쿠키를 여러개 쓰는 것이 가능하니까)
            for(Cookie cookie : cookies){
                // 쿠키 이름이 accessToken인지 확인
                if("accessToken".equals(cookie.getName())){
                    // JWT 토큰값 저장
                    token = cookie.getValue();
                    //찾았으면 종료
                    break;
                }
            }
        }
        // 토큰 존재 여부 확인용 로그
        if(token != null){
            //try catch로 예외처리를 서버가 아닌 401로 리턴
            try{
            LoginInfoDto dto =jwt.checkToken(token);
            if(dto == null){
                response.setStatus(401);
                return;
            }
            }catch(ExpiredJwtException e){//유효시간 지남
                    System.out.println("overTime");
                    response.setStatus(401);
                    return;
            }catch(Exception e){//그냥 토큰이 엉망
                System.out.println("not valid token" + e.getMessage());
                response.setStatus(401);
                return;
            }
        }else{
//            System.out.println("no token");
            response.setStatus(401);
            return;
        }
        System.out.println("valid token");

        filterChain.doFilter(request,response);
    }
    @Override// 이 필터를 거치면 안되는 요청들 설정(doFilterInternal보다 먼저 실행되도록 내부에서 동작)
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        return path.equals("/user/login") || path.equals("/user/signIn");
    }
}
