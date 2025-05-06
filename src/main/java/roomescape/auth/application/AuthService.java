package roomescape.auth.application;

import static roomescape.auth.exception.AuthErrorCode.INVALID_PASSWORD;
import static roomescape.auth.exception.AuthErrorCode.INVALID_TOKEN;
import static roomescape.auth.exception.AuthErrorCode.MEMBER_NOT_FOUND;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = getMemberByEmail(tokenRequest.email());

        if (!tokenRequest.password().equals(member.getPassword())) {
            throw new AuthorizationException(INVALID_PASSWORD);
        }

        String payload = String.valueOf(member.getId());
        String accessToken = jwtTokenProvider.createToken(payload, member.getRole());
        return new TokenResponse(accessToken);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException(MEMBER_NOT_FOUND));
    }

    public Member findMemberByToken(String token) {
        validateMemberToken(token);
        String id = jwtTokenProvider.getPayload(token);
        return getMemberById(Long.parseLong(id));
    }

    private void validateMemberToken(String token) {
        if(!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException(INVALID_TOKEN);
        }
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new AuthorizationException(MEMBER_NOT_FOUND));
    }
}
