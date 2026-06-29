package com.main.personalprojectback.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@NoArgsConstructor
@Getter
@Setter
@Alias("userDto")
public class UserDto {
    // 아이디: 영어문자 4개, 숫자 2개 필수 포함 / 6~8자리 사이 / 특수문자 불가
    @Pattern(//어떤 패턴인지 검사하기 위해 쓰는 라이브러리의 어노테이션
            regexp = "^(?=(?:.*[a-zA-Z]){4})(?=(?:.*[0-9]){2})[a-zA-Z0-9]{6,8}$",//정규식 집어넣는 곳
            message = "아이디는 영어 4개, 숫자 2개를 포함하여 6~8자로 입력해주세요."//반환용 문구
    )
    private String userId;
    // 비밀번호: 영어문자 4개, 숫자 2개 필수 포함 / 최소 8자리 이상 / 특수문자 가능
    @Pattern(
            regexp = "^(?=(?:.*[a-zA-Z]){4})(?=(?:.*[0-9]){2}).{8,}$",
            message = "비밀번호는 영어 4개, 숫자 2개를 포함하여 최소 8자리 이상으로 입력해주세요."
    )
    private String password;
}
