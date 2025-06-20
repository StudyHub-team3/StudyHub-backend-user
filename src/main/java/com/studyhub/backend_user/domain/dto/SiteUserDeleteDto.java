package com.studyhub.backend_user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUserDeleteDto {
    @NotBlank(message = "사용자 비밀번호를 입력하세요.")
    private String password;
}
