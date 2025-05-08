package roomescape.presentation.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.exception.UnauthorizedException;
import roomescape.dto.request.LoginRequestDto;
import roomescape.dto.response.MemberResponseDto;
import roomescape.dto.response.TokenResponseDto;
import roomescape.service.AuthService;

@RestController
public class AuthController {

    public static final String COOKIE_NAME_FOR_TOKEN = "token";
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authService.login(loginRequestDto);
        TokenResponseDto tokenResponseDto = authService.createToken(loginRequestDto.email());

        Cookie cookie = new Cookie(COOKIE_NAME_FOR_TOKEN, tokenResponseDto.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public MemberResponseDto loginCheck(HttpServletRequest request) {
        String tokenFromCookie = getTokenFromCookie(request);
        return authService.getMemberByToken(tokenFromCookie);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie foundCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME_FOR_TOKEN))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("권한이 필요한 접근입니다."));

        return foundCookie.getValue();
    }
}
