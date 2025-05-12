package roomescape.auth.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private static final String secretKey = "ThisIsMySecretKeyYouCannot725AcceptMyZone!!";

    public String createToken(String payload) {
        Claims claims = Jwts.claims();
        claims.setSubject(payload);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String getPayload(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
