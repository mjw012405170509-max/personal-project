package com.main.personalprojectback.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@NoArgsConstructor
@Setter
@Getter
@Alias("lid")
public class LoginInfoDto {
    private String userId;
    private String password;
    private String role;
    private String status;
}
