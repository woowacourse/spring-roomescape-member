package roomescape.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.presentation.methodresolver.AuthInfo;
import roomescape.presentation.methodresolver.AuthPrincipal;
import roomescape.presentation.request.LoginRequest;
import roomescape.presentation.response.LoginCheckResponse;
import roomescape.service.AuthService;
import roomescape.service.result.LoginResult;

@RestController
public class AuthController {

    private final AuthService authService;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {

        LoginResult loginResult = authService.login(loginRequest.toServiceParam());
        ResponseCookie jwtCookie = createCookie("token", loginResult.token());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@AuthPrincipal AuthInfo authInfo) {
        return ResponseEntity.ok().body(new LoginCheckResponse(authInfo.name()));
    }

    private ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) //http 적용전 임시
                .path("/")
                .sameSite("Strict")
                .maxAge(validityInMilliseconds)
                .build();
    }
}
