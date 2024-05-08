package roomescape.auth.presentation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.JwtTokenProvider;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;

@RestController
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthController(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Member member = memberService.findByEmail(request.email());
        String token = jwtTokenProvider.createToken(member.getEmail());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, "token=" + token)
                .header("Keep-Alive", "timeout=60")
                .build();
    }
}
