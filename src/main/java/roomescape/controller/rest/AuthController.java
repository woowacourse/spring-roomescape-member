package roomescape.controller.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.auth.LoginResponse;
import roomescape.global.exception.ApplicationException;
import roomescape.service.AuthService;

import java.util.Arrays;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.login(loginRequest);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> readLoggedInMemberInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractToken(cookies);

        return ResponseEntity.ok(authService.findLoggedInMemberWithToken(token));
    }

    private String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            // TODO: 409 Conflict 터뜨리면 안될듯?
            throw new ApplicationException("쿠키가 존재하지 않습니다.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new ApplicationException("쿠키에 토큰 정보가 존재하지 않습니다."))
                .getValue();
    }
}
