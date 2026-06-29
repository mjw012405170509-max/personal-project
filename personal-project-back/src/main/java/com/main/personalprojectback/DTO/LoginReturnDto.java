package com.main.personalprojectback.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginReturnDto {
    private String userId;
    private String status;
    private String role;
    private String returnMessage;
    private boolean loginStatus;
    private String token;
}
