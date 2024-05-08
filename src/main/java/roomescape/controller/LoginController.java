package roomescape.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.service.AuthService;

@RestController
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String accessKey = authService.makeAccessKey(request);

        Cookie cookie = makeCookie(accessKey);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.getValue())
                .build();
    }

    private Cookie makeCookie(String accessKey) {
        Cookie cookie = new Cookie("token", accessKey);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
