package com.studyhub.backend_user.secret;

import com.studyhub.backend_user.secret.dto.TokenDto;
import com.studyhub.backend_user.secret.props.JwtConfigProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final JwtConfigProperties jwtConfigProperties;
    private volatile SecretKey secretKey;

    private SecretKey getSecretKey() {
        if (secretKey == null) {
            synchronized (this){
                if (secretKey == null) {
                    secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigProperties.getSecretKey()));
                }
            }
        }

        return secretKey;
    }

    public TokenDto.AccessToken generateAccessToken(Long userId, String email, String deviceType) {
        TokenDto.JwtToken access = generateJwtToken(userId, email, deviceType, false);
        return new TokenDto.AccessToken(access);
    }

    public TokenDto.AccessRefreshToken generateAccessRefreshToken(Long userId, String email, String deviceType) {
        TokenDto.JwtToken access = generateJwtToken(userId, email, deviceType, false);
        TokenDto.JwtToken refresh = generateJwtToken(userId, email, deviceType, true);
        return new TokenDto.AccessRefreshToken(access, refresh);
    }

    private TokenDto.JwtToken generateJwtToken(Long userId, String email, String deviceType, boolean refreshToken) {
        int tokenExpireIn = getTokenExpireIn(refreshToken, deviceType); // ms
        String tokenType = refreshToken ? "refresh" : "access";

        String token = Jwts.builder()
                .issuer("studyhub")
                .subject(userId.toString())
                .claim("email", email)
                .claim("deviceType", deviceType)
                .claim("tokenType", tokenType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpireIn * 1000L))
                .signWith(getSecretKey())
                .header().add("type", "JWT")
                .and()
                .compact();

        return new TokenDto.JwtToken(token, tokenExpireIn);
    }

    private int getTokenExpireIn(boolean refreshToken, String deviceType) {
        int tokenExpireIn = 60 * 15; // 15분 (AccessToken Default)

        if (refreshToken) {
            if ("MOBILE".equals(deviceType)) {
                tokenExpireIn = jwtConfigProperties.getMobileExpireIn();
            }
            else if ("TABLET".equals(deviceType)) {
                tokenExpireIn = jwtConfigProperties.getTabletExpireIn();
            }
            else {
                tokenExpireIn = jwtConfigProperties.getExpireIn();
            }
        }

        return tokenExpireIn;
    }
}
