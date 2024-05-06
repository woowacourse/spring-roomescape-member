package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest,
                                        HttpServletResponse servletResponse) {
        TokenResponse tokenResponse = authService.createToken(loginRequest);
        servletResponse.addCookie(createToken(tokenResponse));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Cookie createToken(TokenResponse tokenResponse) {
        Cookie token = new Cookie("token", tokenResponse.accessToken());
        token.setPath("/");
        token.setHttpOnly(true);
        return token;
    }
}
