package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private static final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(long id, LocalDateTime createdAt, Duration tokenLifeTime) {
        LocalDateTime rawExpiredTime = createdAt.plus(tokenLifeTime);
        Date expiredTime = localDateTimeToDate(rawExpiredTime);
        return Jwts.builder()
                .expiration(expiredTime)
                .claim("user_id", id)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private static Date localDateTimeToDate(LocalDateTime expiredTime) {
        Instant instant = expiredTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
