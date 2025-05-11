package roomescape.member.login.authorization;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private static final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String getPayloadEmail(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getPayloadRole(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}
