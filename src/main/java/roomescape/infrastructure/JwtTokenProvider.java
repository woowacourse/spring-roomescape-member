package roomescape.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import roomescape.dto.MemberModel;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String createToken(MemberModel member) {
        return Jwts.builder()
                .setSubject(member.id().toString())
                .claim("name", member.name())
                .claim("role", member.role())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long findTokenSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }
    
    public String findTokenRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }
}

