package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.UnAuthorizedException;
import roomescape.service.result.MemberResult;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private int validityInMilliseconds;

    public String createToken(final MemberResult member) {
        Date expirationDate = new Date(System.currentTimeMillis() + validityInMilliseconds);

        return Jwts.builder()
                .subject(member.id().toString()) //ID와 ROLE이 들어가도록
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long extractIdFromToken(final String token) {
        if (token == null || token.isBlank()) {
            throw new UnAuthorizedException();
        }

        Claims claims = extractAllClaimsFromToken(token);
        return Long.valueOf(claims.getSubject());
    }

    private Claims extractAllClaimsFromToken(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException();
        }
    }
}
