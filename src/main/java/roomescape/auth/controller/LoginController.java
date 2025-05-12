package roomescape.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.request.TokenRequest;
import roomescape.auth.dto.response.LoginCheckResponse;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginMember;
import roomescape.member.service.MemberService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;
    private final MemberService memberService;

    public LoginController(final AuthService authService, final MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody TokenRequest request) {
        String jwt = authService.createToken(request);
        String cookieValue = "accessToken=" + jwt + "; Path=/; HttpOnly; Secure; SameSite=Strict";

        return ResponseEntity.ok()
                .header("Set-Cookie", cookieValue)
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> checkLoggedInUser(LoginMember loginMember) {
        Member member = memberService.getMember(loginMember.id());
        return ResponseEntity.ok().body(new LoginCheckResponse(member.getName()));
    }
}
