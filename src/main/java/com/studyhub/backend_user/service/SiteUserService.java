package com.studyhub.backend_user.service;

import com.studyhub.backend_user.common.exception.BadParameter;
import com.studyhub.backend_user.common.exception.NotFound;
import com.studyhub.backend_user.domain.SiteUser;
import com.studyhub.backend_user.domain.dto.*;
import com.studyhub.backend_user.domain.repository.SiteUserRepository;
import com.studyhub.backend_user.secret.dto.TokenDto;
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
    private final TokenService tokenService;

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

        if (!SecureHashUtils.match(loginDto.getPassword(), user.getPassword())) {
            throw new BadParameter("아이디 혹은 비밀번호를 확인하세요.");
        }

        // TODO: deviceType을 header에서 추출하도록 수정
        return tokenService.generateAccessRefreshToken(user, "WEB");
    }

    @Transactional
    public void logout(SiteUserLogoutDto logoutDto) {
        tokenService.revokeToken(logoutDto.getToken());
    }

    @Transactional
    public TokenDto.AccessRefreshToken refresh(Long userId, SiteUserRefreshDto refreshDto) {
        SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new NotFound("찾을 수 없는 사용자입니다."));

        return tokenService.refreshToken(user, refreshDto.getToken(), "WEB");
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
