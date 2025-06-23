package com.studyhub.backend_user.api.open;

import com.studyhub.backend_user.common.dto.ApiResponseDto;
import com.studyhub.backend_user.domain.dto.SiteUserLoginDto;
import com.studyhub.backend_user.domain.dto.SiteUserLogoutDto;
import com.studyhub.backend_user.domain.dto.SiteUserRefreshDto;
import com.studyhub.backend_user.domain.dto.SiteUserRegisterDto;
import com.studyhub.backend_user.secret.dto.TokenDto;
import com.studyhub.backend_user.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthController {
    private final SiteUserService siteUserService;

    @PostMapping(value = "/login")
    public ApiResponseDto<TokenDto.AccessRefreshToken> login(@RequestBody @Valid SiteUserLoginDto loginDto) {
        TokenDto.AccessRefreshToken accessRefreshToken = siteUserService.login(loginDto);
        return ApiResponseDto.createOk(accessRefreshToken);
    }

    @PostMapping(value = "/register")
    public ApiResponseDto<String> register(@RequestBody @Valid SiteUserRegisterDto registerDto) {
        siteUserService.register(registerDto);
        return ApiResponseDto.defaultOk();
    }

    @PostMapping(value = "/refresh")
    public ApiResponseDto<TokenDto.AccessRefreshToken> refresh(
            @RequestHeader(value = "X-Auth-UserId") String userId,
            @RequestBody @Valid SiteUserRefreshDto refreshDto) {
        TokenDto.AccessRefreshToken accessRefreshToken = siteUserService.refresh(Long.parseLong(userId), refreshDto);
        return ApiResponseDto.createOk(accessRefreshToken);
    }

    @PostMapping(value = "/logout")
    public ApiResponseDto<String> logout(@RequestBody @Valid SiteUserLogoutDto logoutDto) {
        siteUserService.logout(logoutDto);
        return ApiResponseDto.defaultOk();
    }
}
