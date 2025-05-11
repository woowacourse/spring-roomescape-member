package roomescape.auth.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") String secret,
            @Value("${security.jwt.token.expire-length}") long validityInMilliseconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String payload) {
        Claims claims = Jwts.claims().subject(payload).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public String getPayload(final String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        return parser.parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
