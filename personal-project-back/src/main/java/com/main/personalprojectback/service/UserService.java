package com.main.personalprojectback.service;

import com.main.personalprojectback.DTO.LoginDto;
import com.main.personalprojectback.DTO.LoginInfoDto;
import com.main.personalprojectback.DTO.LoginReturnDto;
import com.main.personalprojectback.DTO.UserDto;
import com.main.personalprojectback.dao.UserDao;
import com.main.personalprojectback.global.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor//bcrypt security에서 만든거 가져올려고
@Service
public class UserService {
    @Autowired
    UserDao dao;
    @Autowired
    JwtUtils jwt;

    private final PasswordEncoder pwEncoder;//bcryptEncoder security에서 만든 버전

    @Transactional
    public int signIn(@Valid UserDto user) {
        String originalPw=user.getPassword();
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String newPassword = bcrypt.encode(originalPw);
        user.setPassword(newPassword);
        int result = dao.signIn(user);
        return result;
    }

    public int idCheck(String userId) {
        int result = dao.idCheck(userId);
        return result;
    }

    public LoginReturnDto login(LoginDto user) {
        LoginReturnDto lrd = new LoginReturnDto();
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String userId = user.getUserId();
        LoginInfoDto info = dao.getLoginInfo(userId);
        if(info.getPassword() != null){
            String dbPassword = info.getPassword();
            String urlPassword = user.getPassword();
            boolean bool=bcrypt.matches(urlPassword,dbPassword);
            if(bool){
                String status=info.getStatus();
                if("pending".equals(status)){
                    lrd.setLoginStatus(false);
                    lrd.setReturnMessage("관리자가 미승인한 계정입니다.");
                    return lrd;
                }else{
                    String token=jwt.createToken(userId,info.getRole());
                    lrd.setLoginStatus(true);
                    lrd.setUserId(userId);
                    lrd.setRole(info.getRole());
                    lrd.setStatus(status);
                    lrd.setReturnMessage("success");
                    lrd.setToken(token);
                    return lrd;
                }
            }
        }
        lrd.setLoginStatus(false);
        lrd.setReturnMessage("아이디 또는 비밀번호가 존재하지 않습니다.");
        return lrd;
    }
}
