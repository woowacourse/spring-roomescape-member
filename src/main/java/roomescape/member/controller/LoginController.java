package roomescape.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.LoggedInMember;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.LoginResponse;
import roomescape.member.service.AuthService;

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
    public ResponseEntity<LoginResponse> loginCheck(LoggedInMember member) {
        String name = member.name();
        return ResponseEntity.ok(new LoginResponse(name));
    }
}
