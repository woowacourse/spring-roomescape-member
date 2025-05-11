package roomescape.auth.application;

import org.springframework.stereotype.Service;
import roomescape.auth.domain.Payload;
import roomescape.auth.domain.Token;
import roomescape.auth.infrastructure.Authenticator;
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginRequest;

@Service
public class AuthService {
    private final Authenticator authenticator;
    private final MemberService memberService;

    public AuthService(Authenticator authenticator, MemberService memberService) {
        this.authenticator = authenticator;
        this.memberService = memberService;
    }

    public Token login(LoginRequest loginRequest) {
        Member member = memberService.findByEmailAndPassword(loginRequest);
        return authenticator.authenticate(Payload.from(member));
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
