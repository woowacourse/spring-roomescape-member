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

@Component
public class TokenManager {
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

    public String createToken(long memberId) {
        Date now = Date.from(clock.instant());
        Date expiresAt = new Date(now.getTime() + tokenExpirationMills);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(secretKey)
                .compact();
    }

    public long getMemberIdFrom(String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }
}
