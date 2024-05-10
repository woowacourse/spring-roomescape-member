package roomescape.domain.auth;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class TokenProvider {
    private static final String SECRET_KEY = "Idontcareifthekeyisstolen";
    private static final String EMAIL_FIELD = "email";

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getEmail())
                .signWith(HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public String getPayload(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
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
