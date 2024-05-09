package roomescape.controller.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.member.dto.CookieMemberResponse;
import roomescape.controller.member.dto.LoginMember;
import roomescape.controller.member.dto.MemberLoginRequest;
import roomescape.service.MemberService;
import roomescape.service.TokenResponse;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;

    public LoginController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> login(@RequestBody final MemberLoginRequest memberLoginRequest,
                                               final HttpServletResponse response) {
        // TODO cookie 예쁘게 굽기

        final TokenResponse token = memberService.createToken(memberLoginRequest);

        final Cookie cookie = new Cookie("token", token.assessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Keep-Alive", "timeout=600");
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/check")
    public ResponseEntity<CookieMemberResponse> check(final LoginMember loginMember) {
        if (loginMember != null) {
            return ResponseEntity.ok(new CookieMemberResponse(loginMember.name()));
        }
        return ResponseEntity.ok()
                .build();
    }
}
