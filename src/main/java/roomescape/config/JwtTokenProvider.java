package roomescape.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Role;

@Component
public class JwtTokenProvider {

    public static final String TOKEN_COOKIE_NAME = "token";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String createToken(final Long id, final String role) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("role", role)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private Claims extractClaim(final HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Role extractRole(final HttpServletRequest request) {
        Claims claims = extractClaim(request);
        String role = claims.get("role", String.class);
        return Role.valueOf(role);
    }

    public Long extractMemberId(final HttpServletRequest request) {
        Claims claims = extractClaim(request);
        return Long.valueOf(claims.getSubject());
    }

    public boolean doesNotRequestHasCookie(final HttpServletRequest request) {
        return request.getCookies() == null;
    }

    public boolean doesNotRequestHasToken(final HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .noneMatch(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()));
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
