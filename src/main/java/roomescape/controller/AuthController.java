package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.auth.LoginResponse;
import roomescape.global.dto.response.ApiResponse;
import roomescape.service.AuthService;

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
}
