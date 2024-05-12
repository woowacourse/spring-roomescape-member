package roomescape.global.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
            throw new UnauthorizedException(ErrorType.EXPIRED_TOKEN, ErrorType.EXPIRED_TOKEN.getDescription(), e);
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException(ErrorType.UNSUPPORTED_TOKEN, ErrorType.UNSUPPORTED_TOKEN.getDescription(), e);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(ErrorType.MALFORMED_TOKEN, ErrorType.MALFORMED_TOKEN.getDescription(), e);
        } catch (SignatureException e) {
            throw new UnauthorizedException(ErrorType.INVALID_SIGNATURE_TOKEN, ErrorType.INVALID_SIGNATURE_TOKEN.getDescription(), e);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(ErrorType.ILLEGAL_TOKEN, ErrorType.ILLEGAL_TOKEN.getDescription(), e);
        }
    }
}
