package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.LoginCheckResponse;
import roomescape.auth.dto.response.TokenResponse;
import roomescape.auth.service.AuthService;
import roomescape.cofig.LoginMember;
import roomescape.member.Member;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> tokenLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = authService.createToken(loginRequest);
        setCookie(response, token);
        response.setHeader("Keep-Alive", "timeout=60");
        return ResponseEntity.ok().build();
    }

    private static void setCookie(HttpServletResponse response, TokenResponse token) {
        Cookie cookie = new Cookie("token", token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@LoginMember Member member) {
        final LoginCheckResponse response = new LoginCheckResponse(member.getName());
        return ResponseEntity.ok(response);
    }
}
