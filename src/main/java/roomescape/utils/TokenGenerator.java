package roomescape.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenGenerator {
    private final String secretToken = "secret-token-test";
    private final long validityInMilliseconds = 3600000;

    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretToken)
                .compact();
    }

    public String getPayload(String token) {
        return Jwts.parser()
                .setSigningKey(secretToken)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
