package roomescape.member.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.AuthenticationException;
import roomescape.member.domain.MemberId;
import roomescape.member.domain.MemberName;

@Component
public class JwtTokenExtractor {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    public MemberName extractMemberNameFromCookie(Cookie[] cookies) {
        String name = extractClaimsFromToken(extractTokenFromCookie(cookies))
                .get("name").toString();
        return MemberName.from(name);
    }

    public MemberId extractMemberIdFromCookie(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        Long id = Long.valueOf(extractClaimsFromToken(token).getSubject());
        return MemberId.from(id);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthenticationException("Cookie가 존재하지 않습니다.");
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new IllegalStateException("Cookie에 Token 값이 존재하지 않습니다.");
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
