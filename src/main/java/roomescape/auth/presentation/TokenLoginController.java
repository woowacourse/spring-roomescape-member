package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.application.AuthService;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;

@Controller
public class TokenLoginController {
    private final AuthService authService;

    public TokenLoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {

        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        Cookie cookie = new Cookie("token", tokenResponse.accessToken());
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "/")
                .build();
    }
}
