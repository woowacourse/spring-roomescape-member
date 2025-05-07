package roomescape.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.UnauthorizedException;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}")
            String secretKey,
            @Value("${security.jwt.token.expire-length}")
            long validityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String makeAccessToken(long id, String name) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("name", name)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        validateToken(token);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private void validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            boolean isExpiration = claims.getBody().getExpiration().before(new Date());
            if (isExpiration) {
                throw new IllegalArgumentException("[ERROR] 토큰 유효기간이 지났습니다.");
            }
        } catch (JwtException | IllegalArgumentException exception) {
            throw new UnauthorizedException("[ERROR] 유효하지 않은 인증정보입니다.");
        }
    }
}
