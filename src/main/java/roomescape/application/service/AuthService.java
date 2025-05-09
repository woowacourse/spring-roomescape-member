package roomescape.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;

@Service
public class AuthService {

    private final MemberDao memberDao;

    @Value("${jwt.secret.key}")
    private String secretKey;

    public AuthService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getLoginMember(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Long memberId = extractMemberIdFromToken(token);

        return memberDao.findById(memberId)
                .orElse(null);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private Long extractMemberIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }
}
