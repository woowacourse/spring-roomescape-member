package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    private static final String SECRET_KEY = "zangsu";
    private static final long EXPIRE_LEN = 360000;

    public String generateWith(Map<String, Object> claimDatas) {
        Date now = new Date();
        return Jwts.builder()
                .addClaims(Jwts.claims(claimDatas))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRE_LEN))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
