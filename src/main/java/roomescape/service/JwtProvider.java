package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.AuthException;

@Component
public class JwtProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;


    public String issue(JwtPayload jwtPayload) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claims()
                .add("memberId", jwtPayload.memberId())
                .add("name", jwtPayload.name())
                .and()
                .signWith(getSecretKey())
                .compact();
    }

    public JwtPayload extractPayload(String token) {
        Claims claims = extractClaims(token);
        return new JwtPayload(
                claims.get("memberId", Long.class),
                claims.get("name", String.class)
        );
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AuthException("만료된 토큰입니다.", e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException("잘못된 형식의 토큰입니다.", e);
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public record JwtPayload(Long memberId, String name) {
    }
}
