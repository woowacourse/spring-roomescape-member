package roomescape.presentation.controller;

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
import roomescape.business.service.AuthService;
import roomescape.presentation.dto.LoginCheckResponse;
import roomescape.presentation.dto.LoginRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest, HttpServletResponse response) {
        final String accessToken = authService.login(loginRequest.email(), loginRequest.password());

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(final HttpServletRequest request) {
        final String accessToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName()
                        .equals("token"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보가 없습니다."))
                .getValue();
        final LoginCheckResponse loginCheckResponse = authService.getMemberNameByAccessToken(accessToken);
        return ResponseEntity.ok(loginCheckResponse);
    }
}
