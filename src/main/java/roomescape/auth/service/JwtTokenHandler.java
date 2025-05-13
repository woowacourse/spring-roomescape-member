package roomescape.auth.service;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.common.exception.AuthenticationException;
import roomescape.member.domain.Role;

@Component
public class JwtTokenHandler {

    private static final String TOKEN_NAME = "token";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(final String payload, final String role) {
        Claims claims = Jwts.claims()
                .setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractTokenValue(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationException("쿠키가 존재하지 않습니다.");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public String getMemberIdFromTokenWithValidate(final String token) {
        validateToken(token);
        return getBody(token)
                .getSubject();
    }

    public void validateToken(final String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationException("사용할 수 없는 토큰입니다.");
        }
    }

    public Role getRole(final String token) {
        if (token == null || token.isEmpty()) {
            throw new AuthenticationException("토큰이 존재하지 않습니다.");
        }

        String role = getBody(token)
                .get("role", String.class);

        return Role.from(role);
    }

    private Claims getBody(final String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
