package roomescape.auth.login.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager {

    private final static String SECRET_KEY = "92b355b62d70a2b55fbc27f11023a53203415d617022a4f35a4ca1a23679eafe17057643b931b43b72392674338c832bf0e2eed21cf8e80dec11db5543860f0f";
    private final static int EXPIRATION_TIME = 60 * 30;

    public static String crateToken(final Long id, final String role) {
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");

        return Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .subject(id.toString())
                .claim("role", role)
                .issuedAt(new Date())
                .signWith(key, SIG.HS256)
                .compact();
    }

    public static Long getId(final String token) {
        return Long.valueOf(Jwts.parser()
                .verifyWith(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public static String getRole(final String token) {
        return Jwts.parser()
                .verifyWith(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}
