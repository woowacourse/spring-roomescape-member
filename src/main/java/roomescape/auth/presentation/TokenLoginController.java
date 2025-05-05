package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.AuthService;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.dto.UserResponse;
import roomescape.auth.infrastructure.AuthorizationExtractor;

@RestController
@RequestMapping("/login")
public class TokenLoginController {
    private final AuthService authService;
    private final AuthorizationExtractor<String> authorizationExtractor;

    public TokenLoginController(AuthService authService, AuthorizationExtractor<String> authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {

        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        Cookie cookie = new Cookie("token", tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "/")
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<UserResponse> checkUser(HttpServletRequest request) {
        String token = authorizationExtractor.extract(request);
        UserResponse userResponse = authService.findUserByToken(token);
        return ResponseEntity.ok(userResponse);
    }
}
