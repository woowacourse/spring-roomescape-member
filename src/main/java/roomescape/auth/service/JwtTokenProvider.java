package roomescape.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.entity.User;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String SECRET_KEY;
    @Value("${security.jwt.token.expire-length}")
    private long EXPIRE_LENGTH_MILLI;

    public String createToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE_LENGTH_MILLI);
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
