package roomescape.global.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.UnauthorizedException;

import java.util.Date;

@Component
public class JwtHandler {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long expireTime;

    public String createToken(Long memberId) {
        Date date = new Date();
        Date expiredAt = new Date(date.getTime() + expireTime);

        return Jwts.builder()
                .claim("memberId", memberId)
                .setIssuedAt(date)
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public Long getMemberIdFromToken(String token) {
        validateToken(token);

        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token)
                .getBody()
                .get("memberId", Long.class);
    }

    private void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorType.EXPIRED_TOKEN,
                    String.format("Expired JWT Token: %s", e.getMessage()));
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException(ErrorType.UNSUPPORTED_TOKEN,
                    String.format("Unsupported JWT Token: %s", e.getMessage()));
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(ErrorType.MALFORMED_TOKEN,
                    String.format("Malformed JWT Token: %s", e.getMessage()));
        } catch (SignatureException e) {
            throw new UnauthorizedException(ErrorType.INVALID_SIGNATURE_TOKEN,
                    String.format("Can not validate JWT Token Signature: %s", e.getMessage()));
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(ErrorType.ILLEGAL_TOKEN,
                    String.format("JWT claims string is empty: %s", e.getMessage()));
        }
    }
}
