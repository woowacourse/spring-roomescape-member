package roomescape.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import roomescape.domain.Member;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.id().toString())
                .claim("name", member.name())
                .claim("email", member.email())
                .claim("password", member.password())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

