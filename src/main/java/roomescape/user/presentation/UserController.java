package roomescape.user.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.application.service.AuthService;
import roomescape.user.presentation.dto.LoginRequest;
import roomescape.user.presentation.dto.TokenResponse;

@RestController
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = authService.login(loginRequest);
        Cookie cookie = new Cookie("token", token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
