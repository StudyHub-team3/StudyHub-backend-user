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
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final SiteUserService siteUserService;

    @GetMapping(value = "/{userId}/info")
    public ApiResponseDto<SiteUserInfoDto.Response> getUserInfo(@PathVariable(value = "userId") Long userId) {
        SiteUserInfoDto.Response resposne = siteUserService.getUserInfo(userId);
        return ApiResponseDto.createOk(resposne);
    }

    @GetMapping(value = "/my/info")
    public ApiResponseDto<SiteUserInfoDto.Response> getMyInfo(@RequestHeader(value = "X-Auth-UserId") String userId) {
        SiteUserInfoDto.Response resposne = siteUserService.getUserInfo(Long.parseLong(userId));
        return ApiResponseDto.createOk(resposne);
    }

    @PutMapping(value = "/my/info")
    public ApiResponseDto<SiteUserInfoDto.Response> updateMyInfo(
            @RequestHeader(value = "X-Auth-UserId") String userId,
            @RequestBody @Valid SiteUserModifyDto modifyDto) {
        SiteUserInfoDto.Response response = siteUserService.updateUserInfo(Long.parseLong(userId), modifyDto);
        return ApiResponseDto.createOk(response);
    }
}
