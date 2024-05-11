package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.application.member.dto.response.TokenResponse;
import roomescape.domain.member.Member;

@Component
public class TokenManager {
    private static final String TOKEN_COOKIE_NAME = "token";

    private final SecretKey secretKey;

    public TokenManager(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponse createToken(Member member) {
        String token = Jwts.builder()
                .claim("memberId", member.getId())
                .signWith(secretKey)
                .compact();
        return new TokenResponse(token);
    }

    public long getMemberIdFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰 값이 없습니다.");
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
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
        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_NAME))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("토큰 값이 없습니다."));
        return tokenCookie.getValue();
    }
}
