package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.User;
import roomescape.service.TokenService;
import roomescape.service.UserService;
import roomescape.service.dto.request.LoginRequest;
import roomescape.service.dto.response.AuthenticationInfoResponse;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;
    private final TokenService tokenService;

    public LoginController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<Void> postLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User user = userService.findByEmail(loginRequest.email());
        Cookie cookie = new Cookie("token", tokenService.generateTokenOf(user));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<AuthenticationInfoResponse> getAuthenticationInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키를 찾을 수 없습니다.");
        }
        String token = extractTokenFromCookie(cookies);
        String role = tokenService.parseName(token);
        return ResponseEntity.ok()
                .body(new AuthenticationInfoResponse(role));
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("쿠키를 찾을 수 없습니다."))
                .getValue();
    }
}
