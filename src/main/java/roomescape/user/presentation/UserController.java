package roomescape.user.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.application.service.AuthService;
import roomescape.user.infrastructure.AuthorizationExtractor;
import roomescape.user.infrastructure.BearerAuthorizationExtractor;
import roomescape.user.presentation.dto.LoginRequest;
import roomescape.user.presentation.dto.TokenResponse;
import roomescape.user.presentation.dto.UserResponse;

@RestController
@RequestMapping("/login")
public class UserController {

    private final AuthorizationExtractor<String> authorizationExtractor;
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
        this.authorizationExtractor = new BearerAuthorizationExtractor();
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = authService.login(loginRequest);
        Cookie cookie = new Cookie("token", token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<UserResponse> checkLogin(HttpServletRequest request) {
        String token = authorizationExtractor.extract(request);
        return ResponseEntity.ok().body(authService.getUser(token));
    }

}
