package roomescape.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.exception.UnAuthorizationException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtTokenProvider {
    private static final String TOKEN_COOKIE_NAME = "token";
    private final static String ISSUER = "roomescape";

    @Value("${jwt.secret}")
    private String key;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }


    public String createToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole().toString())
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(secretKey)
                .compact();
    }

    public Long extractId(String token) {
        String stringId = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload().getSubject();
        return Long.parseLong(stringId);
    }

    public Role extractRole(String token) {
        String stringRole = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
        return Role.valueOf(stringRole);
    }

    public void validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                throw new UnAuthorizationException("[ERROR] 토큰이 만료되었습니다.");
            }

        } catch (JwtException | IllegalArgumentException e) {
            throw new UnAuthorizationException("[ERROR] 유효하지 않은 토큰입니다.");
        }
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) return "";
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), "token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
