package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
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

    public String getEmailByToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(claims.get("email"));
    }

    public String getRoleByToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(claims.get("role"));
    }
}
