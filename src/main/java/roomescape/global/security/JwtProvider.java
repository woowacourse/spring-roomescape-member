package roomescape.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.TokenIsEmptyException;
import roomescape.auth.exception.TokenIsExpiredException;
import roomescape.auth.exception.TokenIsInvalidException;
import roomescape.auth.exception.TokenParseFailedException;
import roomescape.member.domain.Member;

@Component
public class JwtProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("role", member.getRole().getName())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getMemberId(String token) {
        Claims claims = getClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public String getRoleName(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    private Claims getClaims(String token) {
        if (token == null || token.isEmpty()) {
            throw new TokenIsEmptyException();
        }

        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenIsExpiredException();
        } catch (UnsupportedJwtException e) {
            throw new TokenIsInvalidException();
        } catch (JwtException e) {
            throw new TokenParseFailedException();
        }
    }
}

