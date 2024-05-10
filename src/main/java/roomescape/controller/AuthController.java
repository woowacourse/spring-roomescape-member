package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.infrastructure.CookieExtractor;
import roomescape.service.AuthService;
import roomescape.service.dto.LoginCheckResponse;
import roomescape.service.dto.LoginRequest;

@RestController
public class AuthController {
    private final CookieExtractor cookieExtractor;
    private final AuthService authService;

    public AuthController(CookieExtractor cookieExtractor, AuthService authService) {
        this.cookieExtractor = cookieExtractor;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        Cookie cookie = cookieExtractor.createCookie(token);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpServletRequest request) {
        String token = cookieExtractor.getToken(request.getCookies());
        LoginCheckResponse response = authService.loginCheck(token);
        return ResponseEntity.ok().body(response);
    }
}
