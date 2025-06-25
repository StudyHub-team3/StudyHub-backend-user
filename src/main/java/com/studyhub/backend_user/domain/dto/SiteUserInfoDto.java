package com.studyhub.backend_user.domain.dto;

import com.studyhub.backend_user.domain.SiteUser;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SiteUserInfoDto {
    @Getter
    @Setter
    public static class Request {
        @NotBlank(message = "사용자 아이디 번호를 입력하세요.")
        private String userId;
    }

    @Getter
    @Setter
    public static class Response {
        private String name;
        private String email;
        private String phoneNumber;
        private LocalDateTime createdAt;

        public static SiteUserInfoDto.Response fromEntity(SiteUser siteUser) {
            SiteUserInfoDto.Response dto = new SiteUserInfoDto.Response();

            dto.name = siteUser.getName();
            dto.email = siteUser.getEmail();
            dto.phoneNumber = siteUser.getPhoneNumber();
            dto.createdAt = siteUser.getCreatedAt();

            return dto;
        }
    }
}
