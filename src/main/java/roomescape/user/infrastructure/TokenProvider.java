package roomescape.user.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.user.application.AuthorizationException;

@Component
public class TokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(String payload) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isInvalidToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        }
        catch (JwtException | IllegalArgumentException exception) {
            throw new AuthorizationException();
        }
    }
}
