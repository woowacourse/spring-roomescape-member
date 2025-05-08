package roomescape.user.ui;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.dto.TokenRequest;
import roomescape.user.application.AuthService;

@RestController
public class UserApiController {

    private final AuthService authService;

    public UserApiController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> createToken(@RequestBody TokenRequest tokenRequest) {
        String accessToken = authService.createToken(tokenRequest);
        ResponseCookie cookie = ResponseCookie.from("token", accessToken)
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
