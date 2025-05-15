package roomescape.controller;

import jakarta.validation.Valid;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.dto.LoginResponse;
import roomescape.service.AuthService;

@Slf4j
@RestController
public class AuthController {

    private static final String TOKEN_COOKIE_NAME = "token";

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody final LoginRequest loginRequest) {
        String token = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        log.info("Authenticated token: {}", token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<Object> checkInfo(@CookieValue(TOKEN_COOKIE_NAME) String token) {
        String name = authService.extractName(token);
        log.info("Check info: {}", name);
        return ResponseEntity.ok(new LoginResponse(name));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {

        ResponseCookie deleteCookie = ResponseCookie.from(TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).build();
    }
}
