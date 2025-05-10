package roomescape.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.domain.AuthRole;
import roomescape.auth.domain.AuthTokenProvider;

@Component
public class JwtTokenProvider implements AuthTokenProvider {

    @Value("${security.jwt.access-token.validity-in-milliseconds}")
    private long validityInMilliseconds;

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${security.jwt.access-token.secret-key}") final String secretKeyValue) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyValue.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(final String principal, final AuthRole role) {
        Claims claims = Jwts.claims()
                .subject(principal)
                .build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .claim("role", role.getRoleName())
                .signWith(secretKey)
                .compact();
    }

    public String getPrincipal(final String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Instant getExpiration(final String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .toInstant();
    }

    public AuthRole getRole(final String token) {
        if (token == null || token.isEmpty()) {
            return AuthRole.GUEST;
        }

        String role = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);

        return AuthRole.from(role);
    }

    public boolean validateToken(final String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // TODO: 만료된 토큰에 대한 처리
            return false;
        } catch (JwtException e) {
            // TODO: 형식이 잘못된 토큰에 대한 처리
            return false;
        }
    }
}
