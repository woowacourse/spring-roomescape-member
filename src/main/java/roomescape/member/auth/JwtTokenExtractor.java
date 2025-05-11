package roomescape.member.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.AuthorizationException;
import roomescape.common.exception.ErrorCode;
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
            throw new AuthorizationException("로그인이 필요합니다.", ErrorCode.MUST_BE_MEMBER);
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        throw new AuthorizationException("로그인이 필요합니다.", ErrorCode.MUST_BE_MEMBER);
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
