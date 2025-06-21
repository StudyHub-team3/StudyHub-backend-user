package com.studyhub.backend_user.service;

import com.studyhub.backend_user.common.exception.BadParameter;
import com.studyhub.backend_user.common.exception.NotFound;
import com.studyhub.backend_user.domain.SiteUser;
import com.studyhub.backend_user.domain.dto.SiteUserInfoDto;
import com.studyhub.backend_user.domain.dto.SiteUserLoginDto;
import com.studyhub.backend_user.domain.dto.SiteUserModifyDto;
import com.studyhub.backend_user.domain.dto.SiteUserRegisterDto;
import com.studyhub.backend_user.domain.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;

    @Transactional
    public void register(SiteUserRegisterDto registerDto) {
        SiteUser user = registerDto.toEntity();
        siteUserRepository.save(user);
    }

    @Transactional
    public void login(SiteUserLoginDto loginDto) {
        SiteUser user = siteUserRepository.findByEmail(loginDto.getEmail());

        if (user == null) {
            throw new NotFound("아이디 혹은 비밀번호를 확인하세요.");
        }

        // TODO: 암호화 적용 후 비밀번호 비교 로직 수정
        if (!user.getPassword().equals(loginDto.getPassword())) {
            throw new BadParameter("아이디 혹은 비밀번호를 확인하세요.");
        }

        // TODO: 로그인 성공 시, 토큰 반화
    }

    @Transactional
    public void logout() {
        // TODO: Refresh Token의 폐기 처리
    }

    @Transactional
    public SiteUserInfoDto.Response getUserInfo(Long userId) {
        SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new NotFound("찾을 수 없는 사용자입니다."));

        return SiteUserInfoDto.Response.fromEntity(user);
    }

    @Transactional
    public SiteUserInfoDto.Response updateUserInfo(Long userId, SiteUserModifyDto modifyDto) {
        SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new NotFound("찾을 수 없는 사용자입니다."));

        // TODO: 암호화된 비밀번호 비교 로직 수정
        if (!user.getPassword().equals(modifyDto.getPassword())) {
            throw new BadParameter("사용자 정보를 수정할 수 없습니다.");
        }

        user.updateUser(modifyDto.getName(), modifyDto.getPhoneNumber());

        return SiteUserInfoDto.Response.fromEntity(user);
    }
}
