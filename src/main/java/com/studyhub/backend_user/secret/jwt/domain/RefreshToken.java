package com.studyhub.backend_user.secret.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Getter
@AllArgsConstructor
@RedisHash(value = "refresh_token", timeToLive = 60 * 60 * 24 * 7) // 유효 기간 : 7일
public class RefreshToken {
    @Id
    private Long userId;
    private String jti;
    private Date createdAt;
    private Date expiration;
}
