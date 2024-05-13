package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.application.dto.request.TokenCreationRequest;
import roomescape.application.dto.response.TokenResponse;
import roomescape.auth.Principal;
import roomescape.auth.TokenProvider;
import roomescape.auth.exception.AuthorizationException;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@Service
public class AuthService {
    private static final String WRONG_EMAIL_OR_PASSWORD_MESSAGE = "등록되지 않은 이메일이거나 비밀번호가 틀렸습니다.";

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse authenticateMember(TokenCreationRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException(WRONG_EMAIL_OR_PASSWORD_MESSAGE));
        member.validatePassword(request.password(), WRONG_EMAIL_OR_PASSWORD_MESSAGE);
        String token = tokenProvider.createToken(Long.toString(member.getId()));
        return new TokenResponse(token);
    }

    public Principal createPrincipal(String token) {
        String subject = tokenProvider.extractSubject(token);
        long memberId = Long.parseLong(subject);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthorizationException("인증 정보가 올바르지 않습니다."));
        return Principal.from(member);
    }
}
