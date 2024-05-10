package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

@Component
public class JwtTokenProvider {
    private static final String ROLE = "role";
    private static final String TOKEN = "token";
    private static final String ADMIN = "ADMIN";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim(ROLE, member.getRole())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getMemberIdByCookie(Cookie[] cookies) {
        String memberId = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(extractTokenFromCookie(cookies))
                .getBody().getSubject();
        return Long.parseLong(memberId);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public String getRoleByCookie(Cookie[] cookies) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(extractTokenFromCookie(cookies))
                .getBody();
        return String.valueOf(claims.get(ROLE));
    }

    public boolean checkRoleByCookie(Cookie[] cookies, HttpServletResponse response) {
        String role = getRoleByCookie(cookies);
        if (role == null || !role.equals(ADMIN)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
