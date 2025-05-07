package roomescape.login.presentation;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.auth.jwt.Token;
import roomescape.login.business.service.LoginService;
import roomescape.login.business.service.TokenCookieService;
import roomescape.login.presentation.request.LoginCheckRequest;
import roomescape.login.presentation.request.LoginRequest;
import roomescape.login.presentation.response.LoginCheckResponse;

@RestController
public class LoginController {

    private final LoginService loginService;
    private final TokenCookieService tokenCookieService;

    @Value("${security.jwt.token.access.expire-length}")
    private long expiration;

    public LoginController(final LoginService loginService, final TokenCookieService tokenCookieService) {
        this.loginService = loginService;
        this.tokenCookieService = tokenCookieService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        Token token = loginService.login(request);
        String cookie = tokenCookieService.createTokenCookie(token.accessToken(), expiration);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                .build();
    }

    @GetMapping("/login/check")
    public LoginCheckResponse checkLogin(LoginCheckRequest request) {
        return loginService.checkLogin(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        String cookie = tokenCookieService.createTokenCookie("", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                .build();
    }
}
