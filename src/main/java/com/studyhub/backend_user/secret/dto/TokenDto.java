package com.studyhub.backend_user.secret.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDto {
    @AllArgsConstructor
    public static class JwtToken {
        private String token;
        private Integer expiresIn;
    }

    @AllArgsConstructor
    public static class AccessToken {
        private JwtToken access;
    }

    @AllArgsConstructor
    public static class AccessRefreshToken {
        private JwtToken access;
        private JwtToken refresh;
    }
}
