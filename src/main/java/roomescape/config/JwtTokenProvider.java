package roomescape.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

@Component
public class JwtTokenProvider {

    private final String secretKey;

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String get(Member member) {
        return Jwts.builder()
            .claim(Claims.SUBJECT, member.getId().toString())
            .claim("name", member.getName())
            .claim("email", member.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public Long getMemberIdFromToken(String token) {
        return Long.valueOf(Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload().getSubject());
    }
}
