package roomescape.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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

    public Member findMember(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Long userId = Long.valueOf(claims.getSubject());
        String name = (String) claims.get("name");
        String email = (String) claims.get("email");
        String password = (String) claims.get("password");

        return new Member(userId, name, email, password);
    }
}

