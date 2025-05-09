package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequest;
import roomescape.service.AuthService;

@RestController
public class LoginController {
    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public LoginController(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response, @RequestBody @Valid LoginRequest request) {
        String token = authService.tokenLogin(request);

        System.out.println(token);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

}
