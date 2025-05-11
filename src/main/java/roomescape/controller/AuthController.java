package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.CookieProvider;
import roomescape.auth.JwtExtractor;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberNameResponse;
import roomescape.service.AuthService;

@RestController
public class AuthController {

    private static final String COOKIE_NAME = "token";

    private final AuthService authService;
    private final CookieProvider cookieProvider;
    private final JwtExtractor jwtExtractor;

    public AuthController(final AuthService authService, final CookieProvider cookieProvider,
                          final JwtExtractor jwtExtractor) {
        this.authService = authService;
        this.cookieProvider = cookieProvider;
        this.jwtExtractor = jwtExtractor;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        final String accessToken = authService.createToken(request);
        Cookie cookie = cookieProvider.createCookie(COOKIE_NAME, accessToken);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = jwtExtractor.extractTokenFromCookie(COOKIE_NAME, cookies);
        MemberNameResponse response = authService.checkLogin(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie expireCookie = cookieProvider.createExpireCookie(COOKIE_NAME);
        response.addCookie(expireCookie);
        return ResponseEntity.ok().build();
    }

}
