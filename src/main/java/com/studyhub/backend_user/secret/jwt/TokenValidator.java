package com.studyhub.backend_user.secret.jwt;

import com.studyhub.backend_user.common.exception.BadParameter;
import com.studyhub.backend_user.secret.jwt.domain.BlacklistAccessToken;
import com.studyhub.backend_user.secret.jwt.domain.RefreshToken;
import com.studyhub.backend_user.secret.jwt.props.JwtConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenValidator {
    private final JwtConfigProperties jwtConfigProperties;
    private volatile SecretKey secretKey;

    private SecretKey getSecretKey() {
        if (secretKey == null) {
            synchronized (this) {
                if (secretKey == null) {
                    secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigProperties.getSecretKey()));
                }
            }
        }

        return secretKey;
    }

    public RefreshToken validateRefreshToken(String token) {
        final Claims claims = verifyAndGetClaims(token);

        if (claims == null) {
            throw new BadParameter("올바르지 않은 리프레시 토큰입니다.");
        }

        Date expirationDate = claims.getExpiration();
        if (expirationDate == null || expirationDate.before(new Date())) {
            throw new BadParameter("올바르지 않은 리프레시 토큰입니다.");
        }

        String tokenType = claims.get("tokenType", String.class);
        if (!"refresh".equals(tokenType)) {
            throw new BadParameter("올바르지 않은 리프레시 토큰입니다.");
        }

        return new RefreshToken(
                Long.parseLong(claims.getSubject()),
                claims.getId(),
                claims.getIssuedAt(),
                claims.getExpiration()
        );
    }

    public BlacklistAccessToken validateAccessToken(Long userId, String token) {
        final Claims claims = verifyAndGetClaims(token);

        if (claims == null) {
            throw new BadParameter("올바르지 않은 액세스 토큰입니다.");
        }

        if (!userId.equals(Long.parseLong(claims.getSubject()))) {
            throw new BadParameter("올바르지 않은 액세스 토큰입니다.");
        }

        Date expirationDate = claims.getExpiration();
        if (expirationDate == null || expirationDate.before(new Date())) {
            throw new BadParameter("올바르지 않은 액세스 토큰입니다.");
        }

        String tokenType = claims.get("tokenType", String.class);
        if (!"access".equals(tokenType)) {
            throw new BadParameter("올바르지 않은 액세스 토큰입니다.");
        }

        return new BlacklistAccessToken(claims.getId());
    }

    private Claims verifyAndGetClaims(String token) {
        Claims claims;

        try {
            claims = Jwts
                    .parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }
}
