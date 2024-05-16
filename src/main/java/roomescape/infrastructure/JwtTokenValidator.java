package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import roomescape.service.tokenmanager.TokenValidator;

@Component
@PropertySource("classpath:application-secret.properties")
public class JwtTokenValidator implements TokenValidator {
    private final String secretKey;

    public JwtTokenValidator(@Value("${security.jwt.token.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public void validateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Date expirationDate = claims.getExpiration();
        Date now = new Date();

        if (expirationDate.before(now)) {
            throw new ExpiredJwtException(null, claims, "토큰이 만료되었습니다.");
        }
    }
}
