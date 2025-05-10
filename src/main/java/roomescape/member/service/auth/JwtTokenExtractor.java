package roomescape.member.service.auth;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenExtractor {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    public String extractMemberNameFromCookie(Cookie[] cookies) {
        return extractMemberNameFromToken(extractTokenFromCookie(cookies));
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalStateException("Cookie가 존재하지 않습니다.");
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new IllegalStateException("Cookie에 Token 값이 존재하지 않습니다.");
    }

    private String extractMemberNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("name").toString();
    }
}
