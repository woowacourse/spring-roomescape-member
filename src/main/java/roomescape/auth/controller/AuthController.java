package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.service.UserAuthService;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.LoginResponse;
import roomescape.auth.service.dto.SignupRequest;

@RestController
public class AuthController {
    private final UserAuthService service;

    public AuthController(UserAuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = service.login(request);
        Cookie cookie = new Cookie("token", loginResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {
        service.signup(request);
        return ResponseEntity.ok().build();
    }
}
