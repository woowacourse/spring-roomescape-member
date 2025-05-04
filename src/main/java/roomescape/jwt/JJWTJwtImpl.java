package roomescape.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JJWTJwtImpl implements JwtUtil {

    @Override
    public Authentication getAuthentication(final String secret, final User user) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String tokenValue = Jwts.builder()
                .subject(user.email())
                .signWith(key)
                .compact();

        return new Authentication(tokenValue);
    }
}
