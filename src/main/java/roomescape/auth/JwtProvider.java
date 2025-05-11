package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.entity.Member;

import java.util.Date;

@Component
public class JwtProvider {

    private static final long EXPIRATION_MILLIS = 1000 * 60 * 30;

    private final String secretKey;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(Member member) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + EXPIRATION_MILLIS))
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public Long extractMemberId(String token) {
        return Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().getSubject());
    }
}
