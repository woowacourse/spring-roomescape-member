package roomescape.member.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.application.dto.GetMemberResponse;
import roomescape.member.application.service.MemberService;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.LoginRequest;
import roomescape.member.presentation.dto.MemberNameResponse;
import roomescape.member.presentation.dto.RegisterRequest;
import roomescape.member.presentation.dto.TokenResponse;

@RestController
public class MemberController {

    private static final String SET_COOKIE_KEY = "token";

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = memberService.login(loginRequest);
        Cookie cookie = new Cookie(SET_COOKIE_KEY, token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> checkLogin(Member member) {
        return ResponseEntity.ok().body(new MemberNameResponse(member.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie(SET_COOKIE_KEY, null);
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/members")
    public ResponseEntity<MemberNameResponse> register(@RequestBody RegisterRequest registerRequest) {
        MemberNameResponse response = memberService.signup(registerRequest);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/members")
    public ResponseEntity<List<GetMemberResponse>> getMembers() {
        List<GetMemberResponse> members = memberService.getMembers();
        return ResponseEntity.ok().body(members);
    }
}
