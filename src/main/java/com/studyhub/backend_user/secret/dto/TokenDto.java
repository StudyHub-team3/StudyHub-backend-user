package com.studyhub.backend_user.secret.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDto {
    @Getter
    @AllArgsConstructor
    public static class JwtToken {
        private String token;
        private Integer expiresIn;
    }

    @Getter
    @AllArgsConstructor
    public static class AccessToken {
        private JwtToken access;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class AccessRefreshToken {
        private JwtToken access;
        private JwtToken refresh;
    }
}
