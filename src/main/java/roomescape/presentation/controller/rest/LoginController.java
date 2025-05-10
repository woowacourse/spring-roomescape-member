package roomescape.presentation.controller.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.domain.LoginUser;
import roomescape.business.domain.Role;
import roomescape.business.service.AuthService;
import roomescape.presentation.AuthenticatedUser;
import roomescape.presentation.dto.LoginRequest;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody final LoginRequest loginRequest,
            final HttpServletResponse response
    ) {
        final String accessToken = authService.createToken(loginRequest);
        final Cookie cookieWithAccessToken = createCookieWithAccessToken(accessToken);
        response.addCookie(cookieWithAccessToken);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        final Cookie cookieWithAccessToken = createCookieWithAccessToken(null);
        response.addCookie(cookieWithAccessToken);

        return ResponseEntity.ok().build();
    }

    private Cookie createCookieWithAccessToken(final String accessToken) {
        final Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginUser> checkLogin(@AuthenticatedUser LoginUser loginUser) {
        if (Role.UNKNOWN == loginUser.role()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(loginUser);
        }

        return ResponseEntity.ok(loginUser);
    }
}
