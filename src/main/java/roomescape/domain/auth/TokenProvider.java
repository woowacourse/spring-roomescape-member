package roomescape.domain.auth;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.util.Map;
import java.util.Optional;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class TokenProvider {
    private static final String EMAIL_FIELD = "email";
    private static final String ROLE_FIELD = "role";

    @Value("${jwt.secret}")
    private String secretKey;

    public String createToken(Member member) {
        return Jwts.builder()
                .setClaims(Map.of(EMAIL_FIELD, member.getEmail(), ROLE_FIELD, member.getRole()))
                .signWith(HS256, secretKey.getBytes())
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get(EMAIL_FIELD, String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get(ROLE_FIELD, String.class);
    }

    public Optional<String> extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }
}
