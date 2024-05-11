package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.ExpiredTokenException;
import roomescape.auth.exception.InvalidTokenException;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.Role;

@Component
public class TokenManager {
    public static final String CLAIM_NAME = "name";
    public static final String CLAIM_ROLE = "role";

    private final SecretKey secretKey;
    private final long tokenExpirationMills;
    private final Clock clock;
    private final JwtParser jwtParser;

    public TokenManager(@Value("${jwt.secret}") String secret,
                        @Value("${jwt.expire-in-millis}") long tokenExpirationMills,
                        Clock clock) {
        this.tokenExpirationMills = tokenExpirationMills;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.clock = clock;
        this.jwtParser = Jwts.parserBuilder()
                .setClock(() -> Date.from(clock.instant()))
                .setSigningKey(secretKey)
                .build();
    }

    public String createToken(MemberRole memberRole) {
        Date now = Date.from(clock.instant());
        Date expiresAt = new Date(now.getTime() + tokenExpirationMills);

        return Jwts.builder()
                .setSubject(String.valueOf(memberRole.getMemberId()))
                .claim(CLAIM_NAME, memberRole.getMemberName())
                .claim(CLAIM_ROLE, memberRole.getRoleName())
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(secretKey)
                .compact();
    }

    public MemberRole extract(String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token)
                    .getBody();
            return new MemberRole(
                    Long.parseLong(claims.getSubject()),
                    claims.get(CLAIM_NAME, String.class),
                    Role.from(claims.get(CLAIM_ROLE, String.class))
            );
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(e);
        }
    }
}
