package roomescape.presentation.api.auth;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.auth.AuthService;
import roomescape.application.auth.dto.LoginResult;
import roomescape.presentation.support.methodresolver.AuthInfo;

@RestController
public class AuthController {

    private final AuthService authService;

    @Value("${security.jwt.token.expire-length}")
    private Duration validityDuration;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        LoginResult loginResult = authService.login(loginRequest.toServiceParam());
        ResponseCookie jwtCookie = createCookie("token", loginResult.token(), validityDuration.toMillis());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(AuthInfo authInfo) {
        return ResponseEntity.ok().body(new LoginCheckResponse(authInfo.name()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie jwtCookie = createCookie("token", "", 0);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .build();
    }

    private ResponseCookie createCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) //https 적용전 임시
                .path("/")
                .sameSite("Strict")
                .maxAge(maxAge)
                .build();
    }
}
