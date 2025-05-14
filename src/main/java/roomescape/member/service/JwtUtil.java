package roomescape.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;
import roomescape.member.domain.Member;

@Component
public class JwtUtil {

    private final int jwtExpirationMs;
    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret, @Value("${jwt.expiration}") int jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public Long getMemberIdFromToken(String token) {
        return Long.valueOf(getJwtClaims(token).getSubject());
    }

    private Claims getJwtClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            throw new BadRequestException(ExceptionCause.INVALID_JWT_INVALID_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ExceptionCause.INVALID_JWT_EXPIRED);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ExceptionCause.INVALID_JWT_EMPTY);
        }
    }
}
