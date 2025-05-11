package roomescape.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Clock;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.application.auth.dto.JwtPayload;
import roomescape.application.support.exception.UnauthorizedException;
import roomescape.domain.member.Role;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final Duration validityDuration;
    private final Clock clock;

    public JwtProvider(
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expire-length}") Duration validityDuration,
            Clock clock
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.validityDuration = validityDuration;
        this.clock = clock;
    }

    public String issue(JwtPayload jwtPayload) {
        Date currentDate = Date.from(clock.instant());
        Date expireDate = new Date(currentDate.getTime() + validityDuration.toMillis());

        return Jwts.builder()
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claims()
                .add("memberId", jwtPayload.memberId())
                .add("name", jwtPayload.name())
                .add("role", jwtPayload.role())
                .and()
                .signWith(secretKey)
                .compact();
    }

    public JwtPayload extractPayload(String token) {
        Claims claims = extractClaims(token);
        return new JwtPayload(
                claims.get("memberId", Long.class),
                claims.get("name", String.class),
                Role.valueOf(claims.get("role", String.class))
        );
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("만료된 토큰입니다.", e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("잘못된 형식의 토큰입니다.", e);
        }
    }
}
