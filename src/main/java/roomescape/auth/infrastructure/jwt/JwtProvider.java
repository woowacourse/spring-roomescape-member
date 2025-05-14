package roomescape.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.infrastructure.Role;
import roomescape.globalexception.UnauthorizedException;

@Component
public class JwtProvider {

    private final SecretKey key;
    private final long validityInMilliseconds;

    public JwtProvider(
        @Value("${security.jwt.token.secret-key}") String secretKey,
        @Value("${security.jwt.token.expire-length}") long validityInMilliseconds
    ) {
        this.validityInMilliseconds = validityInMilliseconds;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(JwtPayload jwtPayload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .claim("identifier", jwtPayload.identifier())
            .claim("role", jwtPayload.role())
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public JwtPayload getPayload(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

            String identifier = claims.get("identifier", String.class);
            Role role = Role.valueOf(claims.get("role", String.class));

            return new JwtPayload(identifier, role);
        } catch (Exception e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}
