package roomescape.auth.login.presentation.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.login.infrastructure.JwtTokenManager;
import roomescape.auth.login.presentation.controller.dto.LoginCheckResponse;
import roomescape.auth.login.presentation.controller.dto.LoginMemberInfo;
import roomescape.auth.login.presentation.controller.dto.annotation.LoginMember;
import roomescape.auth.login.presentation.controller.dto.LoginRequest;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@RestController
public class MemberLoginController {

    private final MemberService memberService;

    public MemberLoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        boolean userExist = memberService.isExistsByEmail(request.email());

        if (!userExist) {
            return ResponseEntity.status(401).build();
        }

        Member member = memberService.findByEmail(request.email());
        if (!member.isSamePassword(request.password())) {
            return ResponseEntity.status(401).build();
        }

        String token = JwtTokenManager.crateToken(member.getId(), "MEMBER");

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@LoginMember final LoginMemberInfo info) {
        Member member = memberService.findById(info.id());
        return ResponseEntity.ok().body(new LoginCheckResponse(member.getName()));
    }
}
