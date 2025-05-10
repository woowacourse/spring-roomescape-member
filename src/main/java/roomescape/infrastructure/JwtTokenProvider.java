package roomescape.infrastructure;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("49f3dc679a33556e5a981507bb3427cf58b3a0b02e3578bd15bfd4883036f4b7")
    private String secretKey;
    @Value("3600000")
    private long validityInMilliseconds;

    public String createToken(Member member) {
        Claims claims = Jwts.claims();
        claims.setSubject(member.getId().toString());
        claims.put("name", member.getName());
        claims.put("role", member.getRole().toString());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getSub(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getPayloadByKey(String token, String key) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get(key, String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

}
