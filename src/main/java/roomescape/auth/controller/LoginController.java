package roomescape.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoggedInMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.service.AuthService;

@RestController
public class LoginController {
    private static final String ACCESS_KEY_VALUE = "token";
    private static final int MINUTE = 60;
    private static final int ACCESS_TOKEN_MAX_AGE = 30 * MINUTE;

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String accessKey = authService.makeToken(request);

        ResponseCookie cookie = makeCookie(accessKey);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    private ResponseCookie makeCookie(String accessKey) {
        return ResponseCookie.from(ACCESS_KEY_VALUE, accessKey)
                .httpOnly(true)
                .path("/")
                .maxAge(ACCESS_TOKEN_MAX_AGE)
                .build();
    }

    @GetMapping("/login/check")
    public LoginResponse loginCheck(LoggedInMember member) {
        String name = member.name();
        return new LoginResponse(name);
    }
}
