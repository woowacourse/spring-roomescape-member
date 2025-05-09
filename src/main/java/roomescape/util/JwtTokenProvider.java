package roomescape.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final static String ISSUER = "roomescape";
    private final static String KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private final static SecretKey SECRET_KEY = Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(Member member){
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractName(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build().parseClaimsJws(token)
                .getPayload().get("name", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
