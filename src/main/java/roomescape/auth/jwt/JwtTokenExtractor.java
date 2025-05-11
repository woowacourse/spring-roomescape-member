package roomescape.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.custom.AuthorizationException;

@Component
public class JwtTokenExtractor implements AuthTokenExtractor {

    private final Key key;

    public JwtTokenExtractor(@Value("${security.jwt.token.secret-key}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractMemberIdFromToken(String token) {
        validateValidToken(token);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public String extractMemberRoleFromToken(String token) {
        validateValidToken(token);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    private void validateValidToken(final String token) {
        validateTokenExists(token);
        validateTokenIntegrityAndExpiration(token);
    }

    private void validateTokenExists(final String token) {
        if (token == null || token.isBlank()) {
            throw new AuthorizationException("로그인 토큰이 존재하지 않습니다");
        }
    }

    private void validateTokenIntegrityAndExpiration(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                throw new AuthorizationException("토큰이 만료 되었습니다");
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthorizationException("서명이 올바르지 않거나 잘못된 토큰입니다");
        }
    }
}
