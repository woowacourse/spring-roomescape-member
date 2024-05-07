package roomescape.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String CLAIM = "id";

    private final SecretKey secretKey;
    private final Long expirationTime;

    public JwtTokenProvider(
            @Value("security.jwt.secret-key") String secretKey,
            @Value("security.jwt.expiration-time") Long expirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    public String createToken(String id) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claim(CLAIM, id)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey, SIG.HS256)
                .compact();
    }

    public Long getMemberId(String token) {
        Claims claims = toClaims(token);

        String memberId = claims.get(CLAIM, String.class);
        return Long.parseLong(memberId);
    }

    public Claims toClaims(String token) {
        try {
            Jws<Claims> claimsJws = getClaimsJws(token);

            return claimsJws.getPayload();
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Invalid token");
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Expired token");
        }
    }

    private Jws<Claims> getClaimsJws(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}
