package roomescape.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.service.result.LoginUserResult;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(final LoginUserResult user) {
        return Jwts.builder()
                .subject(user.id().toString())
                .claim("name", user.name())
                .claim("email", user.email())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public String extractTokenFromCookie(final Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("인증 정보를 찾을 수 없습니다.");
    }

    public Long extractIdFromToken(final String token) {
        return Long.valueOf(Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }
}
