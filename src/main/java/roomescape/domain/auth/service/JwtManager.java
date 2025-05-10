package roomescape.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;

@Slf4j
public class JwtManager {

    private static final String ROLE_KEY = "role";

    private final String secretKey;
    private final int validityInMilliseconds;

    public JwtManager(final String secretKey, final int validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(final User user) {
        final Claims claims = Jwts.claims()
                .setSubject(user.getId()
                        .toString());

        claims.put(ROLE_KEY, user.getRole()
                .getRoleName());

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long parseUserId(final String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public boolean validateToken(final String token) {
        try {
            final Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Roles getRole(final String token) {
        final Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token);

        return Roles.from(claims.getBody()
                .get(ROLE_KEY, String.class));
    }
}
