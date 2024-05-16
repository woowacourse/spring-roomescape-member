package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-secret.properties")
public class JwtTokenValidator implements TokenValidator {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public void isTokenValid(String token) {
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
