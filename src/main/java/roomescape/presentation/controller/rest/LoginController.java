package roomescape.presentation.controller.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.AuthService;
import roomescape.presentation.dto.LoginRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @RequestBody final LoginRequest loginRequest,
            final HttpServletResponse response
    ) {
        final String accessToken = authService.createToken(loginRequest);
        final Cookie cookieWithAccessToken = createCookieWithAccessToken(accessToken);
        response.addCookie(cookieWithAccessToken);

        return ResponseEntity.ok().build();
    }

    private Cookie createCookieWithAccessToken(final String accessToken) {
        final Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
