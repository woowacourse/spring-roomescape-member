package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Date;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "spring-roomescape-member-secret-key";
    private static final long VALIDITY_IN_MILLION_SECONDS = 3600000;

    public String createToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLION_SECONDS);

        return Jwts.builder()
                .setClaims(claims)
                .claim("id", member.getId())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Long getMemberIdFromToken(Cookie[] cookies) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(extractTokenFromCookie(cookies))
                .getBody().get("id", Long.class);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }
}

