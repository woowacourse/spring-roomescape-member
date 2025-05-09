package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.UserResponse;
import roomescape.auth.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {

    public static final String TOKEN_COOKIE_NAME = "token";
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public void login(HttpServletResponse response, @RequestBody TokenRequest request) {
        String token = authService.createToken(request);
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public UserResponse checkMember(HttpServletRequest request) {
        String token = extractTokenByCookies(request)
                .orElseThrow(() -> new IllegalArgumentException("인증 토큰이 쿠키에 존재하지 않습니다."));

        return authService.getUserData(token);
    }

    private Optional<String> extractTokenByCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

}
