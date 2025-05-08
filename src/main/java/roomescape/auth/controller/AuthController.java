package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.service.UserAuthService;
import roomescape.auth.service.dto.response.CheckResponse;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.response.LoginResponse;
import roomescape.auth.service.dto.request.SignupRequest;

@RestController
public class AuthController {
    private final UserAuthService service;

    public AuthController(UserAuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = service.login(request);
        Cookie cookie = new Cookie("token", loginResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                CheckResponse response = service.checkLogin(cookie.getValue());
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request) {
        service.signup(request);
        return ResponseEntity.ok().build();
    }
}
