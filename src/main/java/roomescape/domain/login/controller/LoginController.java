package roomescape.domain.login.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.login.dto.LoginRequest;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberResponse;
import roomescape.domain.member.MemberService;
import roomescape.global.auth.CookieGenerator;
import roomescape.global.auth.JwtTokenProvider;

@RestController
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginController(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Member member = memberService.findMemberByEmailAndPassword(loginRequest.email(),
                loginRequest.password());
        Cookie cookie = CookieGenerator.generate("token",
                jwtTokenProvider.generateToken(String.valueOf(member.getId())));
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new MemberResponse(member.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(HttpServletResponse response) {
        Cookie cookie = CookieGenerator.generateExpiredToken("token");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
