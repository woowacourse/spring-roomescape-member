package roomescape.infrastructure.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;
import roomescape.application.TokenProvider;
import roomescape.exception.RoomescapeErrorCode;
import roomescape.exception.RoomescapeException;

@Component
public class JwtTokenProvider implements TokenProvider {
    private final JwtTokenProperties jwtTokenProperties;

    public JwtTokenProvider(JwtTokenProperties jwtTokenProperties) {
        this.jwtTokenProperties = jwtTokenProperties;
    }

    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtTokenProperties.getExpireMinute());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, jwtTokenProperties.getSecretKey())
                .compact();
    }

    public String getPayload(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtTokenProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new RoomescapeException(RoomescapeErrorCode.TOKEN_EXPIRED);
        }
    }
}
