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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import roomescape.error.TokenCreationException;
import roomescape.error.UnauthorizedException;

@Component
@Slf4j
public class JwtTokenProvider implements TokenProvider {

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

    @Override
    public String createToken(final Claims claims) {
        final Instant now = Instant.now();
        log.info("JWT 토큰 생성 시작");
        try {
            return Jwts.builder()
                    .claims(claims)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plus(validity)))
                    .signWith(key)
                    .compact();
        } catch (IllegalArgumentException | JwtException e) {
            log.error("JWT 토큰 생성 실패", e);
            throw new TokenCreationException("토큰 생성 중 오류가 발생했습니다.");
        }
    }

    @Override
    public String extractPrincipal(final String token) {
        return parseAllClaims(token).getSubject();
    }

    private Claims parseAllClaims(final String token) {
        try {
            final Jwt<?, Claims> jwt = jwtParser.parseSignedClaims(token);
            return jwt.getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}

