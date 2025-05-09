package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.service.AuthService;
import roomescape.error.UnauthorizedException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private static final String COOKIE_TOKEN = "token";
    private final AuthService authService;

    @PostMapping("/login")
    public void login(@RequestBody final LoginRequest request, final HttpServletResponse response) {
        log.info("로그인 시작 {}", request);
        final String token = authService.createToken(request);

        final ResponseCookie cookie = ResponseCookie.from(COOKIE_TOKEN, token)
                .httpOnly(true)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.info("로그인 성공");
    }

    @PostMapping("/logout")
    public void logout(final HttpServletResponse response) {
        final ResponseCookie cookie = ResponseCookie.from(COOKIE_TOKEN, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @GetMapping("/login/check")
    public LoginCheckResponse checkLogin(@CookieValue(name = COOKIE_TOKEN, required = false) String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("토큰 쿠키가 존재하지 않습니다.");
        }
        return authService.checkLogin(token);
    }
}
