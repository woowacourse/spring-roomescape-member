package roomescape.member;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import roomescape.member.domain.Member;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(String.valueOf(member.id()))
                .claim("email", member.email())
                .claim("name", member.name())
                .claim("isAdmin", String.valueOf(member.isAdmin()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
