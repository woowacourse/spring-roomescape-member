package roomescape.auth.controller;

import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.dto.TokenRequestDto;
import roomescape.auth.domain.dto.TokenResponseDto;
import roomescape.auth.service.AuthService;
import roomescape.user.domain.dto.UserResponseDto;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody TokenRequestDto tokenRequestDto) {

        TokenResponseDto tokenResponseDto = authService.login(tokenRequestDto);

        ResponseCookie cookie = ResponseCookie
                .from("SESSION", tokenResponseDto.accessToken())
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<UserResponseDto> checkAuth(@CookieValue(name = "SESSION") String token) {
        UserResponseDto userResponseDto = authService.findMemberByToken(token);
        return ResponseEntity.ok().body(userResponseDto);
    }
}
