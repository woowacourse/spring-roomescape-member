package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Role;
import roomescape.exceptions.AuthException;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(Long id, Role role) {
        Claims claims = Jwts.claims().setSubject(id.toString());

        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusSeconds(validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .claim("role", role.getName())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String findTokenByCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new AuthException("token을 찾을 수 없습니다.");
    }

    public String findRole(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role")
                .toString();
    }

    public String findMemberIdByToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
