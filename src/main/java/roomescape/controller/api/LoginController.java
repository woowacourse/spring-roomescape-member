package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.service.AuthService;

@Controller
public class LoginController {

    private static final String TOKEN_FIELD = "token";

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void login(@RequestBody final TokenRequest tokenRequest, final HttpServletResponse response) {
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);
        final Cookie cookie = new Cookie(TOKEN_FIELD, tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
