package com.studyhub.backend_user.domain.dto;

import com.studyhub.backend_user.domain.SiteUser;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUserModifyDto {
    @NotBlank(message = "사용자 이름을 입력하세요.")
    private String name;

    @NotBlank(message = "사용자 전화번호를 입력하세요.")
    private String phoneNumber;

    @NotBlank(message = "사용자 비밀번호를 입력하세요.")
    private String password;
}
