package roomescape.global.auth;

import org.springframework.stereotype.Service;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.global.dto.TokenRequest;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public AuthService(JwtProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    public String requestLogin(TokenRequest tokenRequest) {
        Member member = memberService.getMemberOf(tokenRequest.email(), tokenRequest.password());
        return jwtProvider.createToken(JwtRequest.from(member));
    }
}
