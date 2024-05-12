package roomescape.controller.rest;

import java.util.Arrays;
import java.util.Objects;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.controller.rest.request.LoginRequest;
import roomescape.controller.rest.response.LoginCheckResponse;
import roomescape.exception.AuthenticationException;
import roomescape.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest body, HttpServletResponse response) {
        Cookie cookie = loginService.createToken(body);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> check(HttpServletRequest request) {
        Cookie token = Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookie.getName(), "token"))
                .findFirst()
                .orElseThrow(AuthenticationException::new);
        LoginCheckResponse loginCheckResponse = loginService.check(token);
        return ResponseEntity.ok().body(loginCheckResponse);
    }
}
