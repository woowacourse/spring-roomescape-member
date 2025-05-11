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
import roomescape.common.exception.LoginFailException;

@Component
public class JwtTokenHandler {

    private static final String TOKEN_NAME = "token";
    private static final String EMPTY = "";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(final String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractTokenValue(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(EMPTY);
    }

    public String getMemberIdFromTokenWithValidate(final String token) {
        validateToken(token);
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new LoginFailException(e.getMessage());
        }
    }
}
