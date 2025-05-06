package roomescape.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;

@RestController
public class AuthController {

    private static final String TOKEN_NAME = "token";

    private final AuthService authService;

    @Autowired
    public AuthController(
            final AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid final LoginRequest request
    ) {
        final String jwt = authService.generateToken(request);
        final ResponseCookie cookie = ResponseCookie.from(TOKEN_NAME, jwt).build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> check(
            @AuthenticationPrincipal final LoginMember loginMember
    ) {
        final LoginResponse response = new LoginResponse(loginMember.name());
        return ResponseEntity.ok(response);
    }
}
