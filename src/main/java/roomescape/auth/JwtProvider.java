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
import roomescape.domain.Member;

@Component
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expired-milli-seconds}")
    private long validityInMilliSeconds;

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliSeconds);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(member.id().toString())
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getPayload(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build();
        return parser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
