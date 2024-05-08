package roomescape.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.response.LoginResponse;
import roomescape.auth.service.AuthService;
import roomescape.auth.dto.request.LoginRequest;

@RestController
public class AuthController {
    private static final String COOKIE_KEY = "token=";

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginMemberRequest) {
        LoginResponse loginResponse = authService.login(loginMemberRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, COOKIE_KEY + loginResponse.token()).build();
    }
}
