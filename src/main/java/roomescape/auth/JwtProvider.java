package roomescape.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import roomescape.domain.User;

@Component
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expired-milli-seconds}")
    private long validityInMilliSeconds;

    public String createToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliSeconds);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(user.id().toString())
                // TODO: 제거 여부 결정
                // .claim("name", user.name())
                // .claim("role", user.role())
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getPayload(String token) {
        // return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build();
        return parser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
/*

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
*/
}
