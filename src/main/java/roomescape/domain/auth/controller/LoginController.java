package roomescape.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.auth.dto.LoginRequest;
import roomescape.domain.auth.dto.TokenResponse;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest,
                                      final HttpServletResponse response) {
        final TokenResponse tokenResponse = authService.login(loginRequest);

        final Cookie cookie = new Cookie("token", tokenResponse.token());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<UserInfoResponse> check(@CookieValue("token") final String token) {
        final UserInfoResponse userInfo = authService.getUserInfo(token);

        return ResponseEntity.ok(userInfo);
    }
}
