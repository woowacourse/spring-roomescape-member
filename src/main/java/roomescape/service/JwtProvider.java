package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;


    public String issue(Long memberId) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claims()
                .add("memberId", memberId)
                .and()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
