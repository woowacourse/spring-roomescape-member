package roomescape.infra;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public static final String EMAIL_FIELD = "email";
    private final SecretKey secretKey;

    @Value("${jwt.expiration.access-token}")
    private Long accessTokenValidityInMilliSecond;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(String payload) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliSecond);
        return Jwts.builder()
                .issuedAt(now)
                .expiration(validity)
                .claims(Map.of(EMAIL_FIELD, payload))
                .signWith(secretKey)
                .compact();
    }

    public String getPayload(String token) {
        return (String) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(EMAIL_FIELD);
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
