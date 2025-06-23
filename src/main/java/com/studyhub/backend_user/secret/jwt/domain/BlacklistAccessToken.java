package com.studyhub.backend_user.secret.jwt.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "blacklist", timeToLive = 60 * 10) // 유효기간 10분
public class BlacklistAccessToken {
    @Id
    private String jti;
    private Boolean blacked = true;

    @Builder
    public BlacklistAccessToken(String jti) {
        this.jti = jti;
    }
}
