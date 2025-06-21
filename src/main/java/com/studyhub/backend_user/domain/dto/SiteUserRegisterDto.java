package com.studyhub.backend_user.domain.dto;

import com.studyhub.backend_user.domain.SiteUser;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SiteUserRegisterDto {
    @NotBlank(message = "사용자 이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "사용자 이름을 입력하세요.")
    private String name;

    @NotBlank(message = "사용자 전화번호를 입력하세요.")
    private String phoneNumber;

    @NotBlank(message = "사용자 비밀번호를 입력하세요.")
    private String password;

    public SiteUser toEntity() {
        SiteUser siteUser = new SiteUser();

        siteUser.setEmail(this.email);
        siteUser.setName(this.name);
        siteUser.setPhoneNumber(this.phoneNumber);
        siteUser.setPassword(this.password);
        siteUser.setCreatedAt(LocalDateTime.now());
        siteUser.setUpdatedAt(LocalDateTime.now());

        return siteUser;
    }
}
