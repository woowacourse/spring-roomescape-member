package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.CheckResponseDto;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.token.JwtProvider;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public AuthService(JwtProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    public TokenResponse requestLogin(TokenRequest tokenRequest) {
        Member member = memberService.getMemberOf(tokenRequest.email(), tokenRequest.password());
        String token = jwtProvider.createToken(member.getEmail());
        return TokenResponse.from(token);
    }

    public CheckResponseDto authenticateByToken(String value) {
        if (jwtProvider.validateToken(value)) {
            String email = jwtProvider.getTokenSubject(value);
            Member member = memberService.getMemberFrom(email);
            return CheckResponseDto.from(member);
        }
        throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }
}
