package roomescape.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.service.AuthService;

@Controller
public class AuthController {

    private static final long EXPIRED_PERIOD = (long) 1000 * 60 * 60; // 30일

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private String createTokenCookie(String value, long maxAge) {
        return ResponseCookie.from("token", value)
                .maxAge(maxAge)
                .build()
                .toString();
    }

    @GetMapping("/login")
    public String readLoginPage() {
        return "/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        LoginResponse data = authService.login(request);
        String cookie = createTokenCookie(data.accessToken(), EXPIRED_PERIOD);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                .build();
    }

    @ResponseBody
    @GetMapping("/login/check")
    public LoginCheckResponse checkLogin(@CookieValue("token") String token) {
        return authService.checkLogin(token);
    }

    @ResponseBody
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String cookie = createTokenCookie("", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                .build();
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
