package roomescape.auth.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;

@Component
public class JwtTokenProvider implements TokenProvider {

    @Value("${jwt.secret-key}")
    String secretKey;
    @Value("${jwt.expiration}")
    String expiration;
    @Value("${jwt.issure}")
    String issure;

    @Override
    public String create(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .issuer(issure)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
