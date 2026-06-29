package com.main.personalprojectback.dao;

import com.main.personalprojectback.DTO.LoginInfoDto;
import com.main.personalprojectback.DTO.UserDto;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    int signIn(UserDto user);

    int idCheck(String userId);

    LoginInfoDto getLoginInfo(String userId);
}
