package com.studyhub.backend_user.secret.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Getter
@AllArgsConstructor
@RedisHash("refresh_token")
public class RefreshToken {
    @Id
    private Long userId;
    private String jti;
    private Date createdAt;
    private Date expiration;
}
