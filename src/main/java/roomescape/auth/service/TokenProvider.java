package roomescape.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.UnauthorizedException;
import roomescape.member.domain.Member;

import java.util.Date;

@Component
public class TokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validTime = 3600000;

    public String create(Member member) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(member.getId()));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validTime);

        return Jwts.builder()
                .setClaims(claims)
                .claim("role", member.getRole())
                .claim("email", member.getEmail())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String extractMemberEmail(String token) {
        Claims claims = getPayload(token);
        return (String) claims.get("email");
    }

    public Claims getPayload(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            validateExpiration(claims);
            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("인증 정보가 올바르지 않습니다.");
        }
    }

    public void validateExpiration(Claims claims) {
        if (claims.getExpiration().before(new Date())) {
            throw new UnauthorizedException("인증 정보가 올바르지 않습니다.");
        }
    }
}
