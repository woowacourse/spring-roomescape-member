package roomescape.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.MemberCreateRequest;
import roomescape.controller.api.dto.response.TokenResponse;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    public AuthController(final AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> createReservation(@RequestBody final MemberCreateRequest request) {
        final var member = memberService.getMember(request);
        final TokenResponse tokenResponse = authService.createToken(member);

        final ResponseCookie cookie = ResponseCookie.from("accessToken", tokenResponse.getAccessToken())
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
