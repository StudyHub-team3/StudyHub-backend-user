package com.studyhub.backend_user.secret.jwt;

import com.studyhub.backend_user.domain.SiteUser;
import com.studyhub.backend_user.secret.jwt.domain.dto.TokenDto;
import com.studyhub.backend_user.secret.jwt.props.JwtConfigProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenGenerator {
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

    public TokenDto.AccessToken generateAccessToken(SiteUser user) {
        TokenDto.JwtToken access = generateJwtToken(user, false);
        return new TokenDto.AccessToken(access);
    }

    public TokenDto.AccessRefreshToken generateAccessRefreshToken(SiteUser user) {
        TokenDto.JwtToken access = generateJwtToken(user, false);
        TokenDto.JwtToken refresh = generateJwtToken(user, true);
        return new TokenDto.AccessRefreshToken(access, refresh);
    }

    private TokenDto.JwtToken generateJwtToken(SiteUser user, boolean refreshToken) {
        int tokenExpireIn = tokenExpiresIn(refreshToken); // ms
        String tokenType = refreshToken ? "refresh" : "access";

        String jti = UUID.randomUUID().toString();

        String token = Jwts.builder()
                .id(jti)
                .issuer("studyhub")
                .subject(user.getId().toString())
                .claim("userName", user.getName())
                .claim("tokenType", tokenType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpireIn * 1000L))
                .signWith(getSecretKey())
                .header().add("type", "JWT")
                .and()
                .compact();

        return new TokenDto.JwtToken(token, tokenExpireIn);
    }

    private int tokenExpiresIn(boolean refreshToken) {
        int expiresIn = 60 * 10;

        if (refreshToken) {
            expiresIn = jwtConfigProperties.getExpiresIn();
        }

        return expiresIn;
    }
}
