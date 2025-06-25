package com.studyhub.backend_user.service;

import com.studyhub.backend_user.common.exception.BadParameter;
import com.studyhub.backend_user.common.exception.NotFound;
import com.studyhub.backend_user.common.exception.Unauthorized;
import com.studyhub.backend_user.secret.jwt.domain.BlacklistAccessToken;
import com.studyhub.backend_user.secret.jwt.domain.RefreshToken;
import com.studyhub.backend_user.domain.SiteUser;
import com.studyhub.backend_user.domain.dto.*;
import com.studyhub.backend_user.secret.jwt.domain.repository.BlacklistAccessTokenRepository;
import com.studyhub.backend_user.secret.jwt.domain.repository.RefreshTokenRepository;
import com.studyhub.backend_user.domain.repository.SiteUserRepository;
import com.studyhub.backend_user.secret.jwt.TokenGenerator;
import com.studyhub.backend_user.secret.jwt.TokenValidator;
import com.studyhub.backend_user.secret.jwt.domain.dto.TokenDto;
import com.studyhub.backend_user.secret.hash.SecureHashUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistAccessTokenRepository blacklistAccessTokenRepository;
    private final TokenGenerator tokenGenerator;
    private final TokenValidator tokenValidator;

    @Transactional
    public void register(SiteUserRegisterDto registerDto) {
        SiteUser user = registerDto.toEntity();
        siteUserRepository.save(user);
    }

    @Transactional
    public TokenDto.AccessRefreshToken login(SiteUserLoginDto loginDto) {
        SiteUser user = siteUserRepository.findByEmail(loginDto.getEmail());

        if (user == null) {
            throw new NotFound("아이디 혹은 비밀번호를 확인하세요.");
        }

        if (user.getDeleted()) {
            throw new BadParameter("삭제한 사용자 입니다.");
        }

        if (!SecureHashUtils.match(loginDto.getPassword(), user.getPassword())) {
            throw new BadParameter("아이디 혹은 비밀번호를 확인하세요.");
        }

        TokenDto.AccessRefreshToken accessRefreshToken = tokenGenerator.generateAccessRefreshToken(user);

        RefreshToken refreshToken = tokenValidator.validateRefreshToken(accessRefreshToken.getRefresh().getToken());
        refreshTokenRepository.save(refreshToken);

        return accessRefreshToken;
    }

    @Transactional
    public void delete(Long userId, SiteUserDeleteDto deleteDto, String accessToken) {
        SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new NotFound("찾을 수 없는 사용자입니다."));

        if (!SecureHashUtils.match(deleteDto.getPassword(), user.getPassword())) {
            throw new BadParameter("비밀번호를 확인하세요.");
        }

        user.softDelete();

        refreshTokenRepository.deleteById(userId);

        BlacklistAccessToken blackAccessToken = tokenValidator.validateAccessToken(userId, accessToken);

        blacklistAccessTokenRepository.save(blackAccessToken);
    }

    @Transactional
    public void logout(Long userId, String accessToken) {
        refreshTokenRepository.deleteById(userId);

        BlacklistAccessToken blackAccessToken = tokenValidator.validateAccessToken(userId, accessToken);

        blacklistAccessTokenRepository.save(blackAccessToken);
    }

    @Transactional
    public TokenDto.AccessRefreshToken refresh(SiteUserRefreshDto refreshDto) {
        RefreshToken oldRefreshToken = tokenValidator.validateRefreshToken(refreshDto.getToken());

        SiteUser user = siteUserRepository.findById(oldRefreshToken.getUserId()).orElseThrow(() -> new NotFound("찾을 수 없는 사용자입니다."));

        RefreshToken savedRefreshToken = refreshTokenRepository.findById(user.getId()).orElseThrow(() -> new Unauthorized("토큰 재발급 권한이 없습니다."));
        if (!savedRefreshToken.getJti().equals(oldRefreshToken.getJti())) {
            throw new Unauthorized("토큰 재발급 권한이 없습니다.");
        }

        TokenDto.AccessRefreshToken accessRefreshToken = tokenGenerator.generateAccessRefreshToken(user);

        RefreshToken newRefreshToken = tokenValidator.validateRefreshToken(accessRefreshToken.getRefresh().getToken());
        refreshTokenRepository.save(newRefreshToken);

        return accessRefreshToken;
    }

    @Transactional
    public SiteUserInfoDto.Response getUserInfo(Long userId) {
        SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new NotFound("찾을 수 없는 사용자입니다."));

        return SiteUserInfoDto.Response.fromEntity(user);
    }

    @Transactional
    public SiteUserInfoDto.Response updateUserInfo(Long userId, SiteUserModifyDto modifyDto) {
        SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new NotFound("찾을 수 없는 사용자입니다."));

        if (!SecureHashUtils.match(modifyDto.getPassword(), user.getPassword())) {
            throw new BadParameter("사용자 정보를 수정할 수 없습니다.");
        }

        user.updateUser(modifyDto.getName(), modifyDto.getPhoneNumber());

        return SiteUserInfoDto.Response.fromEntity(user);
    }
}
