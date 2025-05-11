package roomescape.controller.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthenticationService;
import roomescape.dto.request.LoginMember;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.global.resolver.CurrentMember;
import roomescape.service.MemberService;

@RestController
@RequestMapping()
public class MemberController {
    private final AuthenticationService authenticationService;
    private final MemberService memberService;

    public MemberController(final AuthenticationService authenticationService, final MemberService memberService) {
        this.authenticationService = authenticationService;
        this.memberService = memberService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginMemberRequest loginMemberRequest,
                                          HttpServletResponse response) {
        String accessToken = memberService.loginMember(loginMemberRequest);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setHeader("Keep-Alive", "timeout=60");

        return ResponseEntity.ok().build();

    }

    @GetMapping("/auth/login/check")
    public ResponseEntity<MemberResponse> loginCheck(@CurrentMember LoginMember member) {
        return ResponseEntity.ok(new MemberResponse(member.id(), member.name().getName()));
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> findAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

}

