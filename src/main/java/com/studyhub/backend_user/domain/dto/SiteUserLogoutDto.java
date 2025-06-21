package com.studyhub.backend_user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUserLogoutDto {
    @NotBlank(message = "리프레시 토큰을 입력 받지 못했습니다.")
    private String token;
}
