package roomescape.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String ROLE_CLAIM_KEY = "role";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expire-length}")
    private long validityInMilliseconds;

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        SecretKey key = getSigningKey();

        return Jwts.builder()
                .subject(member.getId().toString())
                .claim(ROLE_CLAIM_KEY, member.getRole().name())
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        SecretKey key = getSigningKey();

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
