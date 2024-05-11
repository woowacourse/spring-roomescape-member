package roomescape.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.AuthorizationException;

@Component
public class TokenProvider {
    private final SecretKey key;
    private final long expirationMilliseconds;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.expiration}") long expirationMilliseconds) {
        byte[] decoded = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decoded);
        this.expirationMilliseconds = expirationMilliseconds;
    }

    public String createToken(long id) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMilliseconds);
        return Jwts.builder()
                .subject(Long.toString(id))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public String extractSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new AuthorizationException("만료된 토큰입니다.");
        } catch (SignatureException e) {
            throw new AuthorizationException("잘못된 토큰입니다.");
        }
    }
}
