package roomescape.global.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.entity.Member;
import roomescape.auth.entity.Role;
import roomescape.global.exception.badRequest.BadRequestException;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String SECRET_KEY;
    @Value("${security.jwt.token.expire-length}")
    private long EXPIRE_LENGTH_MILLI;

    public String createToken(Member member) {
        Claims claims = Jwts.claims()
                .subject(member.getId().toString())
                .add("role", member.getRole().name())
                .build();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE_LENGTH_MILLI);
        return Jwts.builder()
                .claims(claims)
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Role getRole(String token) {
        Claims claims = getClaims(token);
        try {
            String role = claims.get("role", String.class);
            return Role.valueOf(role);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new BadRequestException("잘못된 형식의 토큰입니다.");
        }
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new BadRequestException("올바르지 않은 토큰입니다.");
        }
    }

    public Long getSubject(String token) {
        try {
            Claims claims = getClaims(token);
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            throw new BadRequestException("잘못된 형식의 토큰입니다.");
        }
    }
}
