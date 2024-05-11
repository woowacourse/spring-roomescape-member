package roomescape.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.SomeResponse;
import roomescape.controller.api.dto.request.MemberCreateRequest;
import roomescape.controller.api.dto.response.TokenResponse;
import roomescape.domain.Member;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    public AuthController(final AuthService authService, final MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> createReservation(@RequestBody final MemberCreateRequest request) {
        final var member = memberService.findMember(request);
        final TokenResponse tokenResponse = authService.createToken(member);

        final ResponseCookie cookie = ResponseCookie.from("accessToken", tokenResponse.getAccessToken())
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<SomeResponse> findAuthInfo(@AuthenticationPrincipal final Member member) {
        final var response = new SomeResponse(member.name().value());
        return ResponseEntity.ok(response);
    }
}
