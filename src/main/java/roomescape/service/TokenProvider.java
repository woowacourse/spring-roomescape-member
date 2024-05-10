package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.exception.UnauthorizedException;

import java.util.Date;

@Component
public class TokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validTime;

    public String create(Member member) {
        String accessToken = Jwts.builder()
                .claim("id", member.getId())
                .claim("email", member.getEmail())
                .setExpiration(new Date(new Date().getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return accessToken;
    }

    public long extractMemberId(String token) {
        Claims claims = getPayload(token);
        return (long) claims.get("id");
    }

    public Claims getPayload(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
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
