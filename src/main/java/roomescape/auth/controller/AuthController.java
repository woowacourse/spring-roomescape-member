package roomescape.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.request.TokenRequest;
import roomescape.auth.dto.response.LoginCheckResponse;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginMember;
import roomescape.member.service.MemberService;

@RestController
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;

    public AuthController(final AuthService authService, final MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody TokenRequest request) {
        String jwt = authService.createToken(request);
        String cookieValue = "accessToken=" + jwt + "; Path=/; HttpOnly; Secure; SameSite=Strict";

        return ResponseEntity.ok()
                .header("Set-Cookie", cookieValue)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLoggedInUser(LoginMember loginMember) {
        Member member = memberService.getMember(loginMember.id());
        return ResponseEntity.ok().body(new LoginCheckResponse(member.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String expiredCookie = "accessToken=; Path=/; HttpOnly; Secure; Max-Age=0; SameSite=Strict";
        return ResponseEntity.ok()
                .header("Set-Cookie", expiredCookie)
                .build();
    }
}
