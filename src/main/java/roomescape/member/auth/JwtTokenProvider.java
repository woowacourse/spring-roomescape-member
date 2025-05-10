package roomescape.member.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import roomescape.member.domain.Member;

@Component
public class JwtTokenProvider {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    public String generateToken(Member member) {
        return  Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName().getValue())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
