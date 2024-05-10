package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.user.Role;

@Component
public class JwtProvider {
    private final SecretKey key;
    private final long expirationMilliseconds;

    public JwtProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMilliseconds) {
        byte[] decoded = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decoded);
        this.expirationMilliseconds = expirationMilliseconds;
    }

    public String createToken(long id, Role role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMilliseconds);
        return Jwts.builder()
                .subject(Long.toString(id))
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public long parseToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }
        String subject = claims.getSubject();
        if (claims.getExpiration().before(new Date())) {
            throw new IllegalArgumentException("토큰이 만료되었습니다.");
        }
        return Long.parseLong(subject);
    }
}
