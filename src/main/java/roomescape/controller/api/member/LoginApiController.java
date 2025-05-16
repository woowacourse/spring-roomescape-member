package roomescape.controller.api.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.api.member.dto.LoginCheckResponse;
import roomescape.controller.api.member.dto.MemberLoginRequest;
import roomescape.controller.api.member.dto.TokenResponse;
import roomescape.controller.helper.LoginMember;
import roomescape.infrastructure.CookieExtractor;
import roomescape.model.Member;
import roomescape.service.MemberService;

@Controller
@RequestMapping("/auth")
public class LoginApiController {

    private final MemberService memberService;
    private final CookieExtractor cookieExtractor;

    public LoginApiController(final MemberService memberService, final CookieExtractor cookieExtractor) {
        this.memberService = memberService;
        this.cookieExtractor = cookieExtractor;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid final MemberLoginRequest request,
                                               final HttpServletResponse response) {
        final String token = memberService.login(request);
        final Cookie cookie = cookieExtractor.createCookie(token, 3600);
        response.addCookie(cookie);
        return ResponseEntity.ok().body(new TokenResponse(token));
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@LoginMember final Member member) {
        final LoginCheckResponse response = memberService.checkLogin(member);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        final Cookie cookie = cookieExtractor.createCookie(null, 0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
