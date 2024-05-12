package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginCheckRequest;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest,
                                        HttpServletResponse servletResponse) {
        TokenResponse tokenResponse = authService.createToken(loginRequest);

        Cookie tokenCookie = createTokenCookie(tokenResponse.accessToken());
        servletResponse.addCookie(tokenCookie);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/login/check")
    ResponseEntity<LoginCheckResponse> checkLogin(@CookieValue String token) {
        LoginCheckRequest loginCheckRequest = new LoginCheckRequest(token);

        LoginCheckResponse loginCheckResponse = authService.checkLogin(loginCheckRequest);

        return ResponseEntity.status(HttpStatus.OK).body(loginCheckResponse);
    }

    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletResponse servletResponse) {
        servletResponse.addCookie(deleteTokenCookie());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Cookie createTokenCookie(String tokenValue) {
        Cookie token = new Cookie("token", tokenValue);
        token.setPath("/");
        token.setHttpOnly(true);
        return token;
    }

    private Cookie deleteTokenCookie() {
        Cookie token = new Cookie("token", "");
        token.setPath("/");
        token.setHttpOnly(true);
        token.setMaxAge(0);
        return token;
    }
}
