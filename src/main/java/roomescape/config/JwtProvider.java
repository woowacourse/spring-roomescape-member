package roomescape.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.AuthorizationException;

@Component
public class JwtProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public String createExpiredToken(String token) {
        if (validateToken(token)) {
            Claims claims = getClaims(token);

            return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(claims.getIssuedAt())
                .setExpiration(new Date(claims.getIssuedAt().getTime() - validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        }
        throw new AuthorizationException("토큰이 만료되었습니다. 다시 로그인해주세요.");
    }

    public String getPayload(String token) {
        if (validateToken(token)) {
            return getClaims(token)
                .getSubject();
        }
        throw new AuthorizationException("토큰이 만료되었습니다. 다시 로그인해주세요.");
    }

    private boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }
}
