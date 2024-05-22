package roomescape.controller.rest;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.configuration.resolver.AccessToken;
import roomescape.controller.rest.request.LoginRequest;
import roomescape.controller.rest.response.LoginResponse;
import roomescape.domain.Member;
import roomescape.service.AuthService;

@RestController
@RequestMapping
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest body, HttpServletResponse response) {
        Cookie cookie = authService.createToken(body);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> check(@AccessToken Member member) {
        return ResponseEntity.ok().body(new LoginResponse(member.getName()));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) throws IOException {
        Cookie cookie = authService.expiredToken();
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
