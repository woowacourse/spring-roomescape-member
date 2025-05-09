package roomescape.presentation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.Authenticated;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.AuthenticatedUserResponse;
import roomescape.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;
    private final int cookieMaxAge;

    public AuthController(
            AuthService authService,
            @Value("${auth.cookie.max-age}") int cookieMaxAge
    ) {
        this.authService = authService;
        this.cookieMaxAge = cookieMaxAge;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
        String token = authService.createToken(request);
        ResponseCookie cookie = ResponseCookie.from("token")
                .value(token)
                .httpOnly(true)
                .maxAge(cookieMaxAge)
                .path("/")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/login/check")
    public AuthenticatedUserResponse getAuthenticatedUser(@Authenticated Long memberId) {
        return authService.getAuthenticatedUser(memberId);
    }
}
