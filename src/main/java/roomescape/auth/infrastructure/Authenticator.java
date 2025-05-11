package roomescape.auth.infrastructure;

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
import roomescape.auth.application.AuthorizationException;
import roomescape.auth.domain.Payload;
import roomescape.auth.domain.Token;

@Component
public class Authenticator {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public Token authenticate(Payload payload) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + validityInMilliseconds);
        String token = Jwts.builder()
                .setSubject(payload.memberId())
                .claim("role", payload.role())
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(getSigningKey())
                .compact();
        return new Token(token);
    }

    public boolean isInvalidAuthentication(Token token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token.value());
            return claims.getBody().getExpiration().before(new Date());
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Payload getPayload(Token token) {
        try {
            Claims claims = getClaims(token);
            String role = claims.get("role", String.class);

            return Payload.of(claims.getSubject(), role);
        }
        catch (JwtException | IllegalArgumentException exception) {
            throw new AuthorizationException();
        }
    }

    private Claims getClaims(Token token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token.value())
                .getBody();
    }

    private SecretKey getSigningKey() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }
}
