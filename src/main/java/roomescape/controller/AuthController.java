package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequestDto;
import roomescape.dto.response.TokenResponseDto;
import roomescape.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authService.login(loginRequestDto);
        TokenResponseDto tokenResponseDto = authService.createToken(loginRequestDto.email());

        response.addCookie(new Cookie("token", tokenResponseDto.token()));
    }
}
