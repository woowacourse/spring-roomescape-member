package roomescape.controller.login;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthorizationExtractor;
import roomescape.service.auth.BearerAuthorizationExtractor;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;
    private final AuthorizationExtractor<String> authorizationExtractor;

    public LoginController(final AuthService authService) {
        this.authService = authService;
        this.authorizationExtractor = new BearerAuthorizationExtractor();
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody final TokenRequest request) {
        final TokenResponse token = authService.createToken(request);
        final ResponseCookie cookie = ResponseCookie.from("token", token.token())
                .path("/")
                .httpOnly(true)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Keep-Alive", "timeout=60")
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> check(@RequestBody final TokenRequest request) {
        return ResponseEntity.ok().build();
    }
}
