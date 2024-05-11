package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.service.AuthService;
import roomescape.global.auth.annotation.MemberId;
import roomescape.global.dto.response.ApiResponse;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestBody final LoginRequest loginRequest, final HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);

        Cookie cookie = new Cookie("token", loginResponse.accessToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ApiResponse.success();
    }

    @GetMapping("/login/check")
    public ApiResponse<LoginCheckResponse> checkLogin(@MemberId final Long memberId) {

        LoginCheckResponse response = authService.checkLogin(memberId);
        return ApiResponse.success(response);
    }
}
