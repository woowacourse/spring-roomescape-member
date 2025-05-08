package roomescape.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import roomescape.config.JwtTokenProvider;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Role;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createTokenByMember(Member member) {
        return jwtTokenProvider.createTokenByMember(member);
    }

    public LoginMember getLoginMemberByToken(String token) {
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);
        long memberId = Long.parseLong(claims.getSubject());
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);

        return new LoginMember(memberId, name, Role.valueOf(role));
    }
}
