package roomescape.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.request.LoginRequest;
import roomescape.service.AuthService;
import roomescape.service.result.LoginResult;

@RestController
public class AuthController {

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {

        LoginResult loginResult = authService.login(loginRequest.toServiceParam());
        String jwtCookie = createCookie("token", loginResult.token());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie)
                .build();
    }

    private String createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) //http 적용전 임시
                .path("/")
                .sameSite("Strict")
                .maxAge(validityInMilliseconds)
                .build()
                .toString();
    }
}
