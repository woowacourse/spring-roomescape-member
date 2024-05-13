package roomescape.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.auth.AuthUser;
import roomescape.member.domain.Role;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {

    public static final String NAME = "name";
    public static final String ROLE = "role";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expirationSeconds;

    public String createAccessToken(AuthUser authUser) {
        Instant expiredAt = Instant.now().plusSeconds(expirationSeconds);

        return Jwts.builder()
                .subject(String.valueOf(authUser.getId()))
                .claim(NAME, authUser.getName())
                .claim(ROLE, authUser.getRole())
                .expiration(Date.from(expiredAt))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public AuthUser parse(String token) {
        Claims payload = getPayload(token);

        Long id = Long.parseLong(payload.getSubject());
        String name = payload.get(NAME, String.class);
        Role role = Role.valueOf(payload.get(ROLE, String.class));
        return new AuthUser(id, name, role);
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
