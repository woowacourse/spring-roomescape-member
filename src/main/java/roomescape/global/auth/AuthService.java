package roomescape.global.auth;

import org.springframework.stereotype.Service;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.global.dto.CheckResponseDto;
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
        return jwtProvider.createToken(member.getEmail());
    }

    public CheckResponseDto authenticateByToken(String value) {
        if (jwtProvider.validateToken(value)) {
            String email = jwtProvider.getTokenSubject(value);
            Member member = memberService.getMemberFrom(email);
            return CheckResponseDto.from(member);
        }
        throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }

    public String getSubject(String value) {
        if (jwtProvider.validateToken(value)) {
            return jwtProvider.getTokenSubject(value);
        }
        throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }
}
