package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.entity.Member;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String SECRET_KEY;
    @Value("${security.jwt.token.expire-length}")
    private long EXPIRE_LENGTH_MILLI;

    public String createToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE_LENGTH_MILLI);
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("email", member.getEmail())
                .claim("role", member.getRole().name())
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public String resolve(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
