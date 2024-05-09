package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.global.dto.response.ApiResponse;
import roomescape.auth.service.AuthService;

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
    public ApiResponse<LoginCheckResponse> checkLogin(final HttpServletRequest request) {
        String token = "";
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        LoginCheckResponse response = authService.getMemberIdFromToken(token);
        return ApiResponse.success(response);
    }
}
