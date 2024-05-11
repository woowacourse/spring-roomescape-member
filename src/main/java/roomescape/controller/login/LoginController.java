package roomescape.controller.login;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.auth.AuthService;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginCredential(@RequestBody final TokenRequest request) {
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

    @GetMapping("/login/check")
    public ResponseEntity<MemberCheckResponse> findInformation(LoginMember member) {
        return ResponseEntity.ok()
                .header("Keep-Alive", "timeout=60")
                .header(HttpHeaders.TRANSFER_ENCODING, "chunked")
                .body(MemberCheckResponse.from(member));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        final ResponseCookie cookie = ResponseCookie.from("token")
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
