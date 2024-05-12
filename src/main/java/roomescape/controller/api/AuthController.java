package roomescape.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.AuthMemberRequest;
import roomescape.controller.api.dto.request.MemberLoginRequest;
import roomescape.controller.api.dto.response.AuthMemberNameResponse;
import roomescape.controller.api.dto.response.MemberResponse;
import roomescape.controller.api.dto.response.TokenResponse;
import roomescape.controller.api.resolver.AuthMember;
import roomescape.infrastructure.CookieProvider;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final MemberService memberService;
    private final CookieProvider cookieProvider;

    public AuthController(final MemberService memberService, final CookieProvider cookieProvider) {
        this.memberService = memberService;
        this.cookieProvider = cookieProvider;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> loginMember(@RequestBody final MemberLoginRequest request) {
        final var output = memberService.findMember(request.toInput());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieProvider.create(MemberResponse.from(output)))
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<AuthMemberNameResponse> getAuthMemberName(@AuthMember final AuthMemberRequest request) {
        return ResponseEntity.ok(AuthMemberNameResponse.from(request));
    }
}
