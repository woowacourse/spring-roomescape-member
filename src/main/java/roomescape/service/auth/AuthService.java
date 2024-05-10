package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.controller.login.TokenRequest;
import roomescape.controller.login.TokenResponse;
import roomescape.domain.Member;
import roomescape.exception.UnauthorizedException;
import roomescape.repository.MemberRepository;
import roomescape.service.auth.exception.MemberNotFoundException;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("존재 하지 않는 로그인 정보 입니다."));
    }

    public Member findMemberByToken(final String token) {
        String payload = jwtTokenProvider.getPayload(token);

        return findMemberByEmail(payload);
    }

    public TokenResponse createToken(final TokenRequest token) {
        validateInformation(token);
        final String accessToken = jwtTokenProvider.createToken(token.email());

        return new TokenResponse(accessToken);
    }

    private void validateInformation(final TokenRequest token) {
        final Optional<Member> member = memberRepository.findByEmail(token.email());
        if (member.isEmpty() || !member.get().getPassword().equals(token.password())) {
            throw new UnauthorizedException("로그인 정보가 일치하지 않습니다.");
        }
    }
}
