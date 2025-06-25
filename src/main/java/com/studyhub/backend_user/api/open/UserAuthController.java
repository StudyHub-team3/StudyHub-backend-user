package com.studyhub.backend_user.api.open;

import com.studyhub.backend_user.common.dto.ApiResponseDto;
import com.studyhub.backend_user.common.exception.Unauthorized;
import com.studyhub.backend_user.domain.dto.SiteUserDeleteDto;
import com.studyhub.backend_user.domain.dto.SiteUserLoginDto;
import com.studyhub.backend_user.domain.dto.SiteUserRefreshDto;
import com.studyhub.backend_user.domain.dto.SiteUserRegisterDto;
import com.studyhub.backend_user.secret.jwt.domain.dto.TokenDto;
import com.studyhub.backend_user.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/delete")
    public ApiResponseDto<String> delete(
            @RequestHeader(value = "Authorization") String authHeader,
            @RequestHeader(value = "X-Auth-UserId") String userId,
            @RequestBody @Valid SiteUserDeleteDto deleteDto
            ) {
        if (!authHeader.startsWith("Bearer")) {
            throw new Unauthorized("인증되지 않은 요청입니다.");
        }

        String accessToken = authHeader.substring(7);

        siteUserService.delete(Long.parseLong(userId), deleteDto, accessToken);

        return ApiResponseDto.defaultOk();
    }

    @PostMapping(value = "/refresh")
    public ApiResponseDto<TokenDto.AccessRefreshToken> refresh(@RequestBody @Valid SiteUserRefreshDto refreshDto) {
        TokenDto.AccessRefreshToken accessRefreshToken = siteUserService.refresh(refreshDto);
        return ApiResponseDto.createOk(accessRefreshToken);
    }

    @PostMapping(value = "/logout")
    public ApiResponseDto<String> logout(
            @RequestHeader(value = "Authorization") String authHeader,
            @RequestHeader(value = "X-Auth-UserId") String userId
    ) {
        if (!authHeader.startsWith("Bearer")) {
            throw new Unauthorized("인증되지 않은 요청입니다.");
        }

        String accessToken = authHeader.substring(7);

        siteUserService.logout(Long.parseLong(userId), accessToken);

        return ApiResponseDto.defaultOk();
    }
}
