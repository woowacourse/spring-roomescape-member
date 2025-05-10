package roomescape.member.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.application.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.LoginRequest;
import roomescape.member.presentation.dto.MemberResponse;
import roomescape.member.presentation.dto.RegisterRequest;
import roomescape.member.presentation.dto.TokenResponse;

@RestController
public class MemberController {

    private static final String SET_COOKIE_KEY = "token";

    private final AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = authService.login(loginRequest);
        Cookie cookie = new Cookie(SET_COOKIE_KEY, token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(Member member) {
        return ResponseEntity.ok().body(new MemberResponse(member.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie(SET_COOKIE_KEY, null);
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> register(@RequestBody RegisterRequest registerRequest) {
        MemberResponse response = authService.signup(registerRequest);
        return ResponseEntity.ok().body(response);
    }
}
