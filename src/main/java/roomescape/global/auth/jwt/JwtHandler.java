package roomescape.global.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.auth.jwt.dto.TokenDto;
import roomescape.global.exception.impl.UnauthorizedException;

@Component
public class JwtHandler {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.access.expire-length}")
    private long accessTokenExpireTime;

    public TokenDto createToken(final Long memberId) {
        Date date = new Date();
        Date accessTokenExpiredAt = new Date(date.getTime() + accessTokenExpireTime);

        String accessToken = Jwts.builder()
                .claim("memberId", memberId)
                .setIssuedAt(date)
                .setExpiration(accessTokenExpiredAt)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();

        return new TokenDto(accessToken);
    }

    public Long getMemberIdFromToken(final String token) {
        validateToken(token);
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token)
                .getBody()
                .get("memberId", Long.class);
    }

    private void validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("ExpiredJwtException");
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException("UnsupportedJwtException");
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException("MalformedJwtException");
        } catch (SignatureException e) {
            throw new UnauthorizedException("SignatureException");
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("IllegalArgumentException");
        }
    }
}
