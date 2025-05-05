package roomescape.global.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.auth.jwt.dto.TokenDto;

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
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token)
                .getBody()
                .get("memberId", Long.class);
    }
}
