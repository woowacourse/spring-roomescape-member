package roomescape.common.util.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtTokenManager {

    private final static String SECRET_KEY = "92b355b62d70a2b55fbc27f11023a53203415d617022a4f35a4ca1a23679eafe17057643b931b43b72392674338c832bf0e2eed21cf8e80dec11db5543860f0f";

    public static String crateToken(Long id, String role) {
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");

        return Jwts.builder()
                .subject(id.toString())
                .claim("role", role)
                .issuedAt(Date.from(Instant.now()))
                .signWith(key, SIG.HS256)
                .compact();
    }

    public static Long getMemberId(String token) {
        return Long.valueOf(Jwts.parser()
                .verifyWith(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }
}
