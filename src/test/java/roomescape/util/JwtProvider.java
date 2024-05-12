package roomescape.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.boot.test.context.TestComponent;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@TestComponent
public class JwtProvider {

    private final String secretKey = "12345678123456781234567812345678";
    private final long validityInMilliseconds = 3_600_000;

    public String createToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey())
                .compact();
    }

    public String getSubject(String token) {
        return parsePayload(token).getSubject();
    }

    private Claims parsePayload(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // TODO: String 대신 SecretKey 타입의 key 객체를 필드에 가질 수 있는 방법이 있을까?
    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
