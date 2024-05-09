package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.application.member.dto.response.TokenResponse;
import roomescape.domain.member.Member;

@Component
public class TokenManager {
    private static final String TOKEN_COOKIE_NAME = "token";

    private final String secret;

    public TokenManager(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public TokenResponse createToken(Member member) {
        String token = Jwts.builder()
                .claim("memberId", member.getId())
                .signWith(getSecretKey())
                .compact();
        return new TokenResponse(token);
    }

    public long getMemberIdFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰 값이 없습니다.");
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("memberId", Long.class);
    }

    public long getMemberIdFromCookies(Cookie[] cookies) {
        String token = extractTokenFromCookies(cookies);
        return getMemberIdFromToken(token);
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
