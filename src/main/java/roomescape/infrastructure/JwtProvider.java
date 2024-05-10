package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.user.Role;

@Component
public class JwtProvider {
    private final SecretKey key;

    @Value("${jwt.expiration}")
    private long expirationMilliseconds;

    public JwtProvider() {
        this.key = Jwts.SIG.HS256.key().build();
    }

    public String createToken(long id, Role role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMilliseconds);
        return Jwts.builder()
                .subject(Long.toString(id))
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

//    public long parseToken(String token) {
//        String subject = Jwts.parser()
//                .verifyWith(key)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
//        return Long.parseLong(subject);
//    }
}
