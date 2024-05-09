package roomescape.auth.presentation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.application.JwtTokenProvider;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.AuthInformationResponse;
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;

@RestController
public class AuthController {
    private static final String TOKEN_COOKIE_KEY = "token";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthController(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        Member member = memberService.findByEmail(request.email());
        String token = jwtTokenProvider.createToken(member.getEmail());
        ResponseCookie cookie = ResponseCookie.from(TOKEN_COOKIE_KEY, token).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<AuthInformationResponse> checkAuthInformation(LoginMember loginMember) {
        return ResponseEntity.ok(new AuthInformationResponse(loginMember.name()));
    }
}
