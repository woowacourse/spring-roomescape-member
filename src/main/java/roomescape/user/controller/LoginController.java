package roomescape.user.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.dto.LoginRequest;
import roomescape.user.dto.LoginResponse;
import roomescape.user.service.AuthService;

@RestController
public class LoginController {
    private static final String ACCESS_KEY_VALUE = "token";
    private static final int TOKEN_MAX_AGE_SECOND = 1800;

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String accessKey = authService.makeAccessKey(request);

        ResponseCookie cookie = makeCookie(accessKey);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    private ResponseCookie makeCookie(String accessKey) {
        return ResponseCookie.from(ACCESS_KEY_VALUE, accessKey)
                .httpOnly(true)
                .path("/")
                .maxAge(TOKEN_MAX_AGE_SECOND)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> loginCheck(@CookieValue(ACCESS_KEY_VALUE) String accessKey) {
        LoginResponse response = authService.findUser(accessKey);
        return ResponseEntity.ok(response);
    }
}
