package com.main.personalprojectback.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginDto {
    private String userId;
    private String password;
}
