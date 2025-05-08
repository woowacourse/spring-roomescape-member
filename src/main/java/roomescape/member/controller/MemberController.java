package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.request.TokenRequest;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.dto.response.TokenResponse;
import roomescape.member.service.AuthService;

@RestController
public class MemberController {

    private final AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> tokenLogin(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        TokenResponse token = authService.createToken(tokenRequest);
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
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        MemberResponse memberByToken = authService.findMemberByToken(token);
        return ResponseEntity.ok(memberByToken);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalStateException("쿠키가 존재하지 않습니다.");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new IllegalStateException("토큰 쿠키가 존재하지 않습니다.");
    }
}
