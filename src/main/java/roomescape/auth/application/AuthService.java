package roomescape.auth.application;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.Payload;
import roomescape.auth.infrastructure.TokenProvider;
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginRequest;

@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthService(TokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    public String createToken(LoginRequest loginRequest) {
        Member member = memberService.findByEmailAndPassword(loginRequest);
        return tokenProvider.createToken(Payload.from(member));
    }

    public String getSubject(String token) {
        validateToken(token);
        return tokenProvider.getSubject(token);
    }

    public String getRoleExpression(String token) {
        Claims claims = tokenProvider.getClaims(token);
        return claims.get("role", String.class);
    }

    private void validateToken(String token) {
        if(tokenProvider.isInvalidToken(token)) {
            throw new AuthorizationException();
        }
    }
}
