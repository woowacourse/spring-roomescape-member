package roomescape.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;
import roomescape.business.model.vo.Authorization;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JJWTJwtUtil implements JwtUtil {

    private final SecretKey key;

    public JJWTJwtUtil(@Value("${jwt.secret}") final String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Authentication getAuthentication(final User user) {
        String tokenValue = Jwts.builder()
                .subject(user.email())
                .signWith(key)
                .compact();

        return new Authentication(tokenValue);
    }

    @Override
    public Authorization getAuthorization(final String tokenValue) {
        final String email = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(tokenValue)
                .getPayload()
                .getSubject();

        return new Authorization(email);
    }

    @Override
    public boolean validateToken(String tokenValue) {
        try {
            Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(tokenValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
