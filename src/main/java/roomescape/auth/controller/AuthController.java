package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.Token;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        Token token = authService.login(loginRequest);
        ResponseCookie responseCookie = setCookie(token);

        return ResponseEntity.ok()
                .header("Set-Cookie", responseCookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public LoginCheckResponse loginCheck(@CookieValue(name = "token", required = false) Cookie cookie) {
        return authService.checkLogin(cookie);
    }

    private ResponseCookie setCookie(Token token) {
        return ResponseCookie.from("token", token.getToken())
                .path("/")
                .sameSite("Strict")
                .httpOnly(true)
                .maxAge(60 * 60 * 24)
                .build();
    }
}
