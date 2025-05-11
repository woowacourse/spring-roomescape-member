package roomescape.auth.login.infrastructure.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager {

    private final static int EXPIRATION_TIME = 60 * 30 * 1000;

    @Value("${secret.key}")
    private String SECRET_KEY;

    public String createToken(final Long id, final String role) {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");

        return Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .subject(id.toString())
                .claim("role", role)
                .issuedAt(new Date())
                .signWith(key, SIG.HS256)
                .compact();
    }

    public Long getId(final String token) {
        validateValidToken(token);

        return Long.valueOf(Jwts.parser()
                .verifyWith(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public String getRole(final String token) {
        validateValidToken(token);

        return Jwts.parser()
                .verifyWith(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    private void validateValidToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
