package roomescape.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthorizationExtractor;
import roomescape.service.auth.CookieAuthorizationExtractor;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;
    private final AuthorizationExtractor<String> authorizationExtractor;

    public LoginController(final AuthService authService) {
        this.authService = authService;
        this.authorizationExtractor = new CookieAuthorizationExtractor();
    }

    @PostMapping
    public ResponseEntity<Void> loginCredential(@RequestBody final TokenRequest request) {
        final TokenResponse token = authService.createToken(request);
        final ResponseCookie cookie = ResponseCookie.from("token", token.token())
                .path("/")
                .httpOnly(true)
                .build();

        return ResponseEntity.ok()
                .header("Keep-Alive", "timeout=60")
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberNameResponse> findInformation(HttpServletRequest request) {
        final String token = authorizationExtractor.extract(request);
        final MemberNameResponse member = authService.findMemberByToken(token);
        return ResponseEntity.ok()
                .header("Keep-Alive", "timeout=60")
                .header(HttpHeaders.TRANSFER_ENCODING, "chunked")
                .body(member);
    }
}
