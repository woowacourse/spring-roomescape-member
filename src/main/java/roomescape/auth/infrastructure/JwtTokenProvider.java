package roomescape.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.exception.error.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;

@Component
public class JwtTokenProvider {

    private static final String NAME_CLAIM_KEY = "name";
    private static final String EMAIL_CLAIM_KEY = "email";
    private static final String ROLE_CLAIM_KEY = "role";

    private final String secretKey;
    private final Long expirationInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret.key}") String secretKey,
            @Value("${security.jwt.token.expiration}") Long expirationInMilliseconds

    ) {
        this.secretKey = secretKey;
        this.expirationInMilliseconds = expirationInMilliseconds;
    }

    public String createToken(Member member) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationInMilliseconds);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim(NAME_CLAIM_KEY, member.getName())
                .claim(EMAIL_CLAIM_KEY, member.getEmail())
                .claim(ROLE_CLAIM_KEY, member.getRole())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public JwtPayload getPayload(String token) {
        try {
            Claims claims = extractClaims(token);

            return new JwtPayload(
                    Long.parseLong(claims.getSubject()),
                    claims.get(NAME_CLAIM_KEY, String.class),
                    claims.get(EMAIL_CLAIM_KEY, String.class),
                    Role.valueOf(claims.get(ROLE_CLAIM_KEY, String.class))
            );
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("만료된 토큰 입니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}
