package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.core.dto.auth.TokenRequest;
import roomescape.core.dto.auth.TokenResponse;
import roomescape.core.dto.user.UserResponse;
import roomescape.core.service.UserService;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginProcess(@RequestBody final TokenRequest request,
                                             final HttpServletResponse response) {
        final TokenResponse tokenResponse = userService.createToken(request);
        response.addCookie(createCookie(tokenResponse));

        return ResponseEntity.ok().build();
    }

    private Cookie createCookie(final TokenResponse tokenResponse) {
        final Cookie cookie = new Cookie("token", tokenResponse.getAccessToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @GetMapping("/login/check")
    public ResponseEntity<UserResponse> checkLogin(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        final String token = extractCookies(cookies);
        final UserResponse response = userService.findMemberByToken(token);

        return ResponseEntity.ok().body(response);
    }

    private String extractCookies(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));
    }
}
