package roomescape.application.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.application.member.dto.response.TokenResponse;
import roomescape.domain.member.Member;

@Component
public class TokenManager {

    @Value("${jwt.secret}")
    private String secret;

    public TokenResponse createToken(Member member) {
        String token = Jwts.builder()
                .claim("userId", member.getId())
                .signWith(getSecretKey())
                .compact();
        return new TokenResponse(token);
    }

    public long parseToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰 값이 없습니다.");
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
