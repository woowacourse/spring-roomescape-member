package roomescape.user.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.application.service.AuthService;
import roomescape.user.domain.User;
import roomescape.user.presentation.dto.LoginRequest;
import roomescape.user.presentation.dto.TokenResponse;
import roomescape.user.presentation.dto.UserResponse;

@RestController
public class UserController {

    private static final String SET_COOKIE_KEY = "token";

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = authService.login(loginRequest);
        Cookie cookie = new Cookie(SET_COOKIE_KEY, token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<UserResponse> checkLogin(User user) {
        return ResponseEntity.ok().body(new UserResponse(user.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie(SET_COOKIE_KEY, null);
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return ResponseEntity.ok().build();
    }
}
