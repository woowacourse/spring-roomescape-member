package roomescape.auth.application;

import org.springframework.stereotype.Service;
import roomescape.auth.domain.Payload;
import roomescape.auth.domain.Token;
import roomescape.auth.infrastructure.Authenticator;
import roomescape.member.application.MemberNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginRequest;
import roomescape.member.infrastructure.MemberRepository;

@Service
public class AuthService {
    private final Authenticator authenticator;
    private final MemberRepository memberRepository;

    public AuthService(Authenticator authenticator, MemberRepository memberRepository) {
        this.authenticator = authenticator;
        this.memberRepository = memberRepository;
    }

    public Token login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(MemberNotFoundException::new);
        return authenticator.authenticate(Payload.from(member));
    }

    public Member findMemberByToken(Token token) {
        Payload payload = getPayload(token);
        return memberRepository.findById(payload.memberId())
                .orElseThrow(AuthorizationException::new);
    }

    public Payload getPayload(Token token) {
        validateToken(token);
        return authenticator.getPayload(token);
    }

    private void validateToken(Token token) {
        if (authenticator.isInvalidAuthentication(token)) {
            throw new AuthorizationException();
        }
    }
}
