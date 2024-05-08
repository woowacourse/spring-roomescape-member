package roomescape;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final long EXPIRED_PERIOD = (long) 1000 * 60 * 60; // 30일

    @Value("${jwt.secret")
    private String jwtSecret;

    public String generate(Member member) {
        long now = new Date().getTime();
        
        return Jwts.builder()
                .claim("email", member.getEmail())
                .setExpiration(new Date(now + EXPIRED_PERIOD))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String decode(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .get("email")
                .toString();
    }
}
