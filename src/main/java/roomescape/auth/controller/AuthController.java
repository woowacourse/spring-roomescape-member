package roomescape.auth.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.Token;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        Token token = authService.login(loginRequest);
        ResponseCookie responseCookie = setCookie(token);

        return ResponseEntity.ok()
                .header("Set-Cookie", responseCookie.toString())
                .build();
    }

    private ResponseCookie setCookie(Token token) {
        return ResponseCookie.from("token", token.getToken())
                .path("/reservation")
                .sameSite("Strict")
                .httpOnly(true)
                .maxAge(60 * 60 * 24)
                .build();
    }
}
