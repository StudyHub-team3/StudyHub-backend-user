package com.studyhub.backend_user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SiteUserRefreshDto {
    @NotBlank(message = "리프레시 토큰을 입력하세요.")
    private String token;
}
