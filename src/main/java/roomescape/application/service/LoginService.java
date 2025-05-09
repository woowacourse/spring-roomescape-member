package roomescape.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.application.dto.LoginRequest;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;

@Service
public class LoginService {

    private final MemberDao memberDao;

    @Value("${jwt.secret.key}")
    private String secretKey;

    public LoginService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String createTokenForAuthenticatedMember(LoginRequest request) {
        Optional<Member> member = memberDao.findByEmail(request.email());
        validateLoginMember(request, member);

        return Jwts.builder()
                .setSubject(member.get().getId().toString())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long extractMemberIdFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        String token = extractTokenFromCookie(cookies);
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void validateLoginMember(LoginRequest request, Optional<Member> member) {
        if (member.isEmpty() || member.get().notMatchesPassword(request.password())) {
            throw new IllegalArgumentException("로그인을 실패했습니다. 정보를 다시 확인해 주세요.");
        }
    }
}
