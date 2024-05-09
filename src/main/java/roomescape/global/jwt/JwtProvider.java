package roomescape.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.auth.AuthUser;

@Component
public class JwtProvider {

    public static final String NAME = "name";

    @Value("${jwt.secret}")
    private String secretKey;

    public String createAccessToken(AuthUser authUser) {
        return Jwts.builder()
                .subject(String.valueOf(authUser.id()))
                .claim(NAME, authUser.name())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public AuthUser parse(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long id = Long.parseLong(payload.getSubject());
        String name = payload.get(NAME, String.class);
        return new AuthUser(id, name);
    }
}
