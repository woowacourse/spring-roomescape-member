package roomescape.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.auth.dto.AuthenticatedMember;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.model.Role;
import roomescape.global.exception.AuthenticationException.InvalidTokenException;
import roomescape.global.util.NumberParser;

@Component
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {

    private static final String CLAIM_EMAIL_KEY = "email";
    private static final String CLAIM_ROLE_KEY = "role";
    private static final String CLAIM_NAME_KEY = "name";

    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;

    @Override
    public String create(Member member) {
        return jwtBuilder
                .subject(member.getId().toString())
                .claim(CLAIM_EMAIL_KEY, member.getEmail())
                .claim(CLAIM_ROLE_KEY, member.getRole().name())
                .claim(CLAIM_NAME_KEY, member.getName())
                .compact();
    }

    @Override
    public AuthenticatedMember resolveAuthenticatedMember(String token) {
        Claims payload = extracClaims(token);

        return AuthenticatedMember.builder()
                .id(NumberParser.parseToLong(payload.getSubject()))
                .email(payload.get(CLAIM_EMAIL_KEY, String.class))
                .role(Role.findByName(payload.get(CLAIM_ROLE_KEY, String.class)))
                .name(payload.get(CLAIM_NAME_KEY, String.class))
                .build();
    }

    private Claims extracClaims(String token) {
        try {
            return jwtParser
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("토큰이 만료되었습니다.", e);
        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다..", e);
        }
    }
}
