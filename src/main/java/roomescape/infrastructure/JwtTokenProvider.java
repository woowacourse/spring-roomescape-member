package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import roomescape.domain.AuthenticationTokenProvider;

@Component
public class JwtTokenProvider implements AuthenticationTokenProvider {

    private static final SecretKey SECRET_KEY = SIG.HS256.key().build();
    private static final long EXPIRATION_DURATION = 900000;

    public String createToken(final String payload) {
        Claims claims = Jwts.claims().subject(payload).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_DURATION);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String getPayload(final String token) {
        return Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean isValidToken(final String token) {
        try {
            var claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            var isExpired = claims.getPayload().getExpiration().before(new Date());
            return !isExpired;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

