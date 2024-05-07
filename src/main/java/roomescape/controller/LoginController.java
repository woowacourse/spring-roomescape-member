package roomescape.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.dto.TokenResponse;
import roomescape.service.AuthService;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        TokenResponse loginToken = authService.login(request);

        ResponseCookie cookie = ResponseCookie
                .from("token", loginToken.accessToken())
                .path("/")
                .httpOnly(true)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
