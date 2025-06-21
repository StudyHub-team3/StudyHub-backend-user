package com.studyhub.backend_user.api.open;

import com.studyhub.backend_user.common.dto.ApiResponseDto;
import com.studyhub.backend_user.domain.dto.SiteUserLoginDto;
import com.studyhub.backend_user.domain.dto.SiteUserRegisterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthController {
    @PostMapping(value = "/login")
    public ApiResponseDto<String> login(@RequestBody @Valid SiteUserLoginDto loginDto) {
        return ApiResponseDto.createOk("AccessRefreshToken");
    }

    @PostMapping(value = "/register")
    public ApiResponseDto<String> register(@RequestBody @Valid SiteUserRegisterDto registerDto) {
        return ApiResponseDto.defaultOk();
    }

    @PostMapping(value = "/logout")
    public ApiResponseDto<String> logout() {
        return ApiResponseDto.defaultOk();
    }
}
