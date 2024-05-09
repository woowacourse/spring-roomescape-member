package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.AuthService;
import roomescape.service.dto.TokenResponse;

@RestController
@RequestMapping("/logout")
public class LogoutController {
    private final AuthService authService;

    public LogoutController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public void tokenLogout(HttpServletRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.extractTokenByCookies(request);
        Cookie cookie = authService.deleteCookieByToken(tokenResponse);
        response.addCookie(cookie);
    }
}
