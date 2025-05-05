package roomescape.auth.application;

import static roomescape.auth.exception.AuthErrorCode.INVALID_PASSWORD;
import static roomescape.auth.exception.AuthErrorCode.INVALID_TOKEN;
import static roomescape.auth.exception.AuthErrorCode.MEMBER_NOT_FOUND;

import org.springframework.stereotype.Service;
import roomescape.auth.domain.Member;
import roomescape.auth.domain.MemberRepository;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = getMember(tokenRequest.email());

        if (!tokenRequest.password().equals(member.getPassword())) {
            throw new AuthorizationException(INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }

    public Member findMemberByToken(String token) {
        validateMemberToken(token);
        String email = jwtTokenProvider.getPayload(token);
        return getMember(email);
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException(MEMBER_NOT_FOUND));
    }

    private void validateMemberToken(String token) {
        if(!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException(INVALID_TOKEN);
        }
    }
}
