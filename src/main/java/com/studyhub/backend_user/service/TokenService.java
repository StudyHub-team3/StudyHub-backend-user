package com.studyhub.backend_user.service;

import com.studyhub.backend_user.common.exception.NotFound;
import com.studyhub.backend_user.common.exception.Unauthorized;
import com.studyhub.backend_user.domain.RefreshToken;
import com.studyhub.backend_user.domain.SiteUser;
import com.studyhub.backend_user.domain.repository.RefreshTokenRepository;
import com.studyhub.backend_user.secret.TokenGenerator;
import com.studyhub.backend_user.secret.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public TokenDto.AccessRefreshToken generateAccessRefreshToken(SiteUser user) {
        TokenDto.AccessRefreshToken accessRefreshToken = tokenGenerator.generateAccessRefreshToken(user.getId(), user.getEmail());

        refreshTokenRepository.save(new RefreshToken(
                user,
                accessRefreshToken.getRefresh().getToken(),
                LocalDateTime.now().plusSeconds(accessRefreshToken.getRefresh().getExpiresIn())
        ));

        return accessRefreshToken;
    }

    @Transactional
    public TokenDto.AccessRefreshToken refreshToken(SiteUser user, String refreshToken) {
        revokeToken(refreshToken);

        return generateAccessRefreshToken(user);
    }

    @Transactional
    public void revokeToken(String refreshToken) {
        RefreshToken foundRefreshToken = refreshTokenRepository.findByToken(refreshToken);

        if (foundRefreshToken == null) {
            throw new NotFound("리프레시 토큰 인증 불가");
        }

        if (!foundRefreshToken.isValid()) {
            throw new Unauthorized("리프레시 토큰 인증 불가");
        }

        foundRefreshToken.revoke();
    }
}
