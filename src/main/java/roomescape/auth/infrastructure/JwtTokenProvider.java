package roomescape.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.dto.AuthenticatedMember;
import roomescape.global.exception.AuthenticationException.InvalidTokenException;
import roomescape.global.util.NumberParser;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Component
public class JwtTokenProvider implements TokenProvider {

    private static final String CLAIM_EMAIL_KEY = "email";
    private static final String CLAIM_ROLE_KEY = "role";
    private static final String CLAIM_NAME_KEY = "name";

    @Value("${jwt.secret-key}")
    String secretKey;
    @Value("${jwt.expiration}")
    int expiration;
    @Value("${jwt.issure}")
    String issure;

    @Override
    public String create(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim(CLAIM_EMAIL_KEY, member.getEmail())
                .claim(CLAIM_ROLE_KEY, member.getRole().name())
                .claim(CLAIM_NAME_KEY, member.getName())
                .issuer(issure)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    //TODO : JWT 유효성 예외 처리
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
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("토큰이 만료되었습니다.", e);
        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다..", e);
        }
    }
}
