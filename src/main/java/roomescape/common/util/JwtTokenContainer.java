package roomescape.common.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.LoginException;
import roomescape.member.domain.Member;

@Component
public class JwtTokenContainer {

    private final SecretKey secretKey;

    public JwtTokenContainer(@Value("${security.jwt.token.secret-key}") String key) {
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String createJwtToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName())
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new LoginException("만료된 토큰입니다.");
        } catch (JwtException e) {
            throw new LoginException("올바르지 않은 토큰 형태입니다.");
        }
    }

    public Long getMemberId(String token) {
        try {
            String id = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return Long.parseLong(id);
        } catch (JwtException e) {
            throw new LoginException("올바르지 않은 토큰 형태입니다.");
        }
    }

    public String getMemberName(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("name").toString();
        } catch (JwtException e) {
            throw new LoginException("올바르지 않은 토큰 형태입니다.");
        }
    }
}
