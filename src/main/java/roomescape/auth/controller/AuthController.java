package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.controller.dto.LoginRequest;
import roomescape.auth.controller.dto.MemberResponse;
import roomescape.auth.service.AuthService;

@Controller
public class AuthController {
    private static final String SESSION_KEY = "token";
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
        authService.authenticate(loginRequest);
        Cookie cookie = new Cookie(SESSION_KEY, authService.createToken(loginRequest).accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        MemberResponse check = authService.fetchByToken(extractTokenFromCookie(request.getCookies()));
        return ResponseEntity.ok().body(check);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_KEY)) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
