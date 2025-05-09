package roomescape.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import roomescape.auth.JwtProperties;
import roomescape.error.UnauthorizedException;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final JwtParser jwtParser;
    private final Duration validity;

    public JwtTokenProvider(final JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
        this.jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
        this.validity = Duration.ofMillis(jwtProperties.getExpireLength());
    }

    public String createToken(final Claims claims) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(validity)))
                .signWith(key)
                .compact();
    }

    public String getSubject(final String token) {
        return parseAllClaims(token).getSubject();
    }

    private Claims parseAllClaims(final String token) {
        try {
            Jwt<?, Claims> jwt = jwtParser.parseSignedClaims(token);
            return jwt.getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}

