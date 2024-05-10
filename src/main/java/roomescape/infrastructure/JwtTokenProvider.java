package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.domain.User;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = "spring-roomescape-member-secret-key";
    private final long validityInMilliseconds = 3600000;

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .claim("id", user.getId())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}

