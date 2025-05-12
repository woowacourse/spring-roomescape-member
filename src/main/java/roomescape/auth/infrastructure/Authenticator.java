package roomescape.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.application.AuthorizationException;
import roomescape.auth.domain.Payload;
import roomescape.auth.domain.Token;

@Component
public class Authenticator {

    public static final String ROLE_CLAIM_EXPRESSION = "role";

    private final SecretKey signingKey;
    private final long validityInMilliseconds;

    public Authenticator(@Value("${security.jwt.token.secret-key}") String secretKey,
                         @Value("${security.jwt.token.expire-length}") long validityInMilliseconds) {

        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public Token authenticate(Payload payload) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + validityInMilliseconds);

        String token = Jwts.builder()
                .setSubject(payload.getMemberIdExpression())
                .claim(ROLE_CLAIM_EXPRESSION, payload.getRoleExpression())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(signingKey)
                .compact();

        return new Token(token);
    }

    public boolean isInvalidAuthentication(Token token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token.value());

            return claims.getBody().getExpiration().before(new Date());
        }
        catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }

    public Payload getPayload(Token token) {
        try {
            Claims claims = getClaims(token);
            String role = claims.get(ROLE_CLAIM_EXPRESSION, String.class);

            return Payload.from(claims.getSubject(), role);
        }
        catch (JwtException | IllegalArgumentException e) {
            throw new AuthorizationException();
        }
    }

    private Claims getClaims(Token token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token.value())
                .getBody();
    }
}
