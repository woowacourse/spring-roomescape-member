package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberSignupRequest signupRequest) {
        return ResponseEntity.created(URI.create("/login"))
                .body(memberService.add(signupRequest));
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
}
