package roomescape.auth.infrastructure.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.service.in.TokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Component
public class JwtProvider implements TokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expire-time}")
    private long expireTime;

    public String issue(Member member) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("role", member.getRole())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public Long getMemberId(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String sub = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return Long.parseLong(sub);
    }

    public Role getRole(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String role = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
        return Role.valueOf(role);
    }
}
