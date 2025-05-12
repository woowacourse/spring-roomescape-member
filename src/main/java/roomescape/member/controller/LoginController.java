package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginMember;
import roomescape.auth.dto.request.TokenRequest;
import roomescape.auth.dto.response.TokenResponse;
import roomescape.auth.service.AuthService;
import roomescape.member.Member;
import roomescape.member.dto.response.MemberResponse;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
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
        cookie.setMaxAge(60);
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(@LoginMember Member member) {
        final MemberResponse response = new MemberResponse(member.getId(), member.getName());
        return ResponseEntity.ok(response);
    }
}
