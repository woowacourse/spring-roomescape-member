package roomescape.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;
import roomescape.business.model.vo.LoginInfo;
import roomescape.business.model.vo.UserRole;

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
                .claim("role", user.role())
                .compact();

        return new Authentication(tokenValue);
    }

    @Override
    public LoginInfo getAuthorization(final String tokenValue) {
        validateToken(tokenValue);
        final Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(tokenValue)
                .getPayload();

        final String email = claims.getSubject();
        final UserRole role = UserRole.valueOf(claims.get("role", String.class));

        return new LoginInfo(email, role);
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
