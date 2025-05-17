package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.controller.dto.LoginRequest;
import roomescape.auth.controller.dto.LoginResponse;
import roomescape.auth.service.AuthService;

import java.util.Arrays;
import java.util.Optional;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login/check")
    public LoginResponse loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies)
                .orElseThrow(() -> new IllegalArgumentException("token 쿠키가 없습니다."));
        return authService.getLoginResponseByToken(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String accessToken = authService.login(request);
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        extractTokenFromCookie(cookies).ifPresent(token -> {
            Cookie expiredCookie = new Cookie("token", null);
            expiredCookie.setMaxAge(0);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            response.addCookie(expiredCookie);
        });
    }

    private Optional<String> extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

}
