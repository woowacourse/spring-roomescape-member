package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Date;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Role;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "spring-roomescape-member-secret-key";
    private static final long VALIDITY_IN_MILLION_SECONDS = 3600000;

    public String createToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getId().toString());
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLION_SECONDS);

        return Jwts.builder()
                .setClaims(claims)
                .claim("name", member.getName())
                .claim("role", member.getRole().toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Long getMemberIdFromToken(Cookie[] cookies) {
        return Long.valueOf(Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(extractTokenFromCookie(cookies))
                .getBody()
                .getSubject());
    }

    public String getMemberNameFromToken(Cookie[] cookies) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(extractTokenFromCookie(cookies))
                .getBody()
                .get("name", String.class);
    }

    public Role getRoleFromToken(Cookie[] cookies) {
        return Role.from(Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(extractTokenFromCookie(cookies))
                .getBody()
                .get("role", String.class));
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키를 찾을 수 없습니다.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }
}

