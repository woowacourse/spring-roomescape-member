package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.LoginUser;
import roomescape.domain.member.Member;
import roomescape.dto.member.LoginCheckResponse;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private static final String TOKEN_KEY = "token";

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberSignupRequest signupRequest) {
        return ResponseEntity.created(URI.create("/login"))
                .body(memberService.add(signupRequest));
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@LoginUser Member member) {
        return ResponseEntity.ok(new LoginCheckResponse(member));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        memberService.checkLoginInfo(loginRequest);
        LoginResponse loginResponse = authService.createToken(loginRequest);
        Cookie cookie = new Cookie(TOKEN_KEY, loginResponse.getAccessToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_KEY, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
