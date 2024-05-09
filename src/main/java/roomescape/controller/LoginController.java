package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.User;
import roomescape.infrastructure.TokenProvider;
import roomescape.service.UserService;
import roomescape.service.dto.request.LoginRequest;
import roomescape.service.dto.response.AuthenticationInfoResponse;

@Controller
public class LoginController {
    private static final String TOKEN_COOKIE_NAME = "token";
    private static final int COOKIE_AGE_SECONDS = 60;

    private final UserService userService;
    private final TokenProvider tokenProvider;

    public LoginController(UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User user = userService.findByEmail(loginRequest.email());
        String token = tokenProvider.generateTokenOf(user);

        ResponseCookie cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .maxAge(COOKIE_AGE_SECONDS)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<AuthenticationInfoResponse> loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키를 찾을 수 없습니다.");
        }
        String token = extractTokenFromCookie(cookies);
        String authenticationInfo = tokenProvider.parseAuthenticationInfo(token);
        return ResponseEntity.ok()
                .body(new AuthenticationInfoResponse(authenticationInfo));
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_NAME))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("쿠키를 찾을 수 없습니다."))
                .getValue();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(TOKEN_COOKIE_NAME)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }
}
