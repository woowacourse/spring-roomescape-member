package roomescape.controller.api;

import io.jsonwebtoken.*;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Role;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String createToken(final Member member) {
        return Jwts.builder()
                .setSubject(member.id().toString())
                .claim("name", member.name())
                .claim("role", member.role())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(final String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateAdmin(final String token) {
        final var role = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);

        return Role.valueOf(role).equals(Role.ADMIN);
    }
}

