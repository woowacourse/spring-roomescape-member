package roomescape.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.ExpiredTokenException;
import roomescape.common.exception.InvalidTokenException;
import roomescape.common.exception.MissingTokenExcpetion;
import roomescape.member.domain.Role;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(final String payload, final Role role) {
        final Claims claims = Jwts.claims().setSubject(payload);
        claims.put("role", role.getValue());
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (final ExpiredJwtException e) {
            throw new ExpiredTokenException("Expired token" + e);
        } catch (final SecurityException | MalformedJwtException e) {
            throw new InvalidTokenException("Invalid token" + e);
        } catch (final Exception e) {
            throw new MissingTokenExcpetion("Missing token" + e);
        }
    }

    public Role getRole(final String token) {
        try {
            return Role.valueOf((String) Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role"));
        } catch (final ExpiredJwtException e) {
            throw new ExpiredTokenException("Expired token" + e);
        } catch (final SecurityException | MalformedJwtException e) {
            throw new InvalidTokenException("Invalid token" + e);
        } catch (final Exception e) {
            throw new MissingTokenExcpetion("Missing token" + e);
        }
    }
}
