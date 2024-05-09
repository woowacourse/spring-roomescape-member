package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.member.MemberService;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public String createToken(final LoginRequest loginRequest) {
        memberService.findByEmail(loginRequest.email());
        return jwtTokenProvider.createToken(loginRequest.email());
    }

    public Member findMemberByToken(final String token) {
        String email = jwtTokenProvider.getPayload(token);
        return memberService.findByEmail(email);
    }
}
