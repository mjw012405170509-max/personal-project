package com.main.personalprojectback.global.security;

import com.main.personalprojectback.DTO.LoginDto;
import com.main.personalprojectback.DTO.LoginInfoDto;
import com.main.personalprojectback.DTO.LoginReturnDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expire-hour}")
    private Integer expireHour;

    // SecretKey 생성을 공통 메서드로 분리하여 코드 중복 제거
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String userId, String role) {

        SecretKey key = getSigningKey();

        Calendar calendar = Calendar.getInstance();
        Date startTime = calendar.getTime();

        calendar.add(Calendar.HOUR, expireHour);
        Date endTime = calendar.getTime();

        String token = Jwts.builder()
                .issuedAt(startTime)
                .expiration(endTime)
                .signWith(key)
                .subject(userId)
                .claim("role",role)
                .compact();
        return token;

    }

//
// 비교하고 검사하는 토큰 로직
    public LoginInfoDto checkToken(String token){
        if(token == null || token.trim().isEmpty()){
            return null;
        }
//        if(token.startsWith("Bearer ")){
//            token = token.substring(7);
//        }else{
//            return null;
//        }
        try{

        SecretKey key = getSigningKey();
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String userId =claims.getSubject();
        String role = claims.get("role",String.class);
        LoginInfoDto login = new LoginInfoDto();
        login.setRole(role);
        login.setUserId(userId);
        return login;
        } catch (ExpiredJwtException e) {
            throw e;   // Filter에게 넘김
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
//public LoginUser checkToken(String token) {
//    try {
//        if (token == null || token.trim().isEmpty()) {
//            return null;
//        }
//
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//
//        SecretKey key = getSigningKey();
//
//        Claims claims = Jwts.parser()
//                .verifyWith(key)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//
//        // [치명적 에러 해결] 캐스팅 에러 방지를 위해 클래스 타입을 명시하여 안전하게 추출
//        Long userId = claims.get("userId", Long.class);
//        String loginId = claims.get("loginId", String.class);
//        String nickname = claims.get("nickname", String.class);
//        String email = claims.get("email", String.class);
//        String role = claims.get("role", String.class);
//        String profileImg = claims.get("profileImg", String.class);
//
//        // String으로 안전하게 꺼낸 뒤 LocalDateTime으로 저장
//        String createdAtStr = claims.get("createdAt", String.class);
//        // --> 만약 회원가입일이 null 혹은 비어있는게 아닌 경우, 문자열 형태에서 다시 LocalDateTime으로 받아오게 하는 로직. 비어잇거나 null일 경우에는 null처리.
//        LocalDateTime createdAt = (createdAtStr != null && !createdAtStr.isEmpty())? LocalDateTime.parse(createdAtStr): null;
//
//
//
//        LoginUser login = new LoginUser();
//        login.setUserId(userId);
//        login.setLoginId(loginId);
//        login.setNickname(nickname);
//        login.setEmail(email);
//        login.setRole(role);
//        login.setProfileImg(profileImg);
//        login.setToken(token);
//        login.setCreatedAt(createdAt);
//
//
//        if (claims.getExpiration() != null) {
//            login.setEndTime(claims.getExpiration().getTime());
//        }
//
//        return login;
//
//    } catch (ExpiredJwtException e) {
//        System.out.println("토큰 만료됨: " + e.getMessage());
//        // 컨트롤러나 필터에서 401 에러 처리를 유도할 수 있도록 설계 필요
//        return null;
//    } catch (JwtException e) {
//        System.out.println("유효하지 않은 토큰 (위변조 등): " + e.getMessage());
//        return null;
//    } catch (Exception e) {
//        System.out.println("토큰 검증 중 예상치 못한 에러 발생: " + e.getMessage());
//        //어디서 터졌는지 추적하기 위해 StackTrace추가
//        e.printStackTrace();
//        return null;
//    }
//}

}
