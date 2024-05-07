package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.exception.AuthorizationException;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider tokenProvider;

    public AuthService(final MemberRepository memberRepository, final JwtTokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        final String inputPassword = tokenRequest.password();
        final Member member = memberRepository.findByEmail(tokenRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (!member.getPassword().equals(inputPassword)) {
            throw new AuthorizationException("비밀번호가 일치하지 않습니다.");
        }
        final String accessToken = tokenProvider.createToken(member);
        return new TokenResponse(accessToken);
    }
}
