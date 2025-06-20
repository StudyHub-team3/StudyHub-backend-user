package com.studyhub.backend_user.api.open;

import com.studyhub.backend_user.common.dto.ApiResponseDto;
import com.studyhub.backend_user.domain.dto.SiteUserInfoDto;
import com.studyhub.backend_user.domain.dto.SiteUserModifyDto;
import com.studyhub.backend_user.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final SiteUserService siteUserService;

    @GetMapping(value = "/info/{userId}")
    public ApiResponseDto<SiteUserInfoDto.Response> getUserInfo(@RequestBody @Valid SiteUserInfoDto.Request infoDto) {
        return ApiResponseDto.createOk(new SiteUserInfoDto.Response());
    }

    @PutMapping(value = "/info/{userId}")
    public ApiResponseDto<SiteUserInfoDto.Response> putUserInfo(@RequestBody @Valid SiteUserModifyDto modifyDto) {
        return ApiResponseDto.createOk(new SiteUserInfoDto.Response());
    }
}
