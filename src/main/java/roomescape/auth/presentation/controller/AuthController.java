package roomescape.auth.presentation.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.service.AuthService;
import roomescape.auth.presentation.dto.LoginRequest;
import roomescape.auth.presentation.dto.TokenResponse;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.MemberNameResponse;

@RestController
public class AuthController {

    private static final String SET_COOKIE_KEY = "token";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = authService.login(loginRequest);
        Cookie cookie = new Cookie(SET_COOKIE_KEY, token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> checkLogin(Member member) {
        return ResponseEntity.ok().body(new MemberNameResponse(member.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie(SET_COOKIE_KEY, null);
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return ResponseEntity.ok().build();
    }
}
