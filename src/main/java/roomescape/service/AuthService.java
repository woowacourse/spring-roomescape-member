package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.token.TokenProvider;
import roomescape.dto.MemberResponse;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.exception.AuthenticationException;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(final MemberRepository memberRepository, final TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        final String inputPassword = tokenRequest.password();
        final Member member = memberRepository.findByEmail(tokenRequest.email())
                .orElseThrow(() -> new AuthenticationException("가입되지 않은 이메일입니다."));
        if (!member.getPassword().equals(inputPassword)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
        final String accessToken = tokenProvider.createToken(member);
        return new TokenResponse(accessToken);
    }

    public MemberResponse findMemberByToken(final String token) {
        final Long memberId = tokenProvider.getMemberId(token);
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 멤버입니다."));
        return new MemberResponse(member);
    }
}
