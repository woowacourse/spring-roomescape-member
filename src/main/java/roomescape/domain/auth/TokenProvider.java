package roomescape.domain.auth;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class TokenProvider {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final String EMAIL_FIELD = "email";
    private static final String ROLE_FIELD = "role";

    public String createToken(Member member) {
        return Jwts.builder()
                .setClaims(Map.of(EMAIL_FIELD, member.getEmail(), ROLE_FIELD, member.getRole()))
                .signWith(HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get(EMAIL_FIELD, String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get(ROLE_FIELD, String.class);
    }

    public String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
