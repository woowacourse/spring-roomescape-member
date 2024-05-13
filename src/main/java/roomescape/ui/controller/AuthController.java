package roomescape.ui.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.AuthService;
import roomescape.application.dto.response.MemberResponse;
import roomescape.application.dto.response.TokenResponse;
import roomescape.auth.Principal;
import roomescape.ui.controller.dto.LoginRequest;
import roomescape.ui.support.AuthenticationPrincipal;

@RestController
public class AuthController {
    private static final String TOKEN_KEY_NAME = "token";
    private static final String EMPTY_TOKEN = "";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.authenticateMember(request.toTokenCreationRequest());
        ResponseCookie cookie = ResponseCookie.from(TOKEN_KEY_NAME, response.token())
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public MemberResponse check(@AuthenticationPrincipal Principal principal) {
        return new MemberResponse(principal.getId(), principal.getName());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from(TOKEN_KEY_NAME, EMPTY_TOKEN)
                .maxAge(0)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
