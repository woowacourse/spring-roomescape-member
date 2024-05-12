package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.controller.api.dto.request.MemberAuthRequest;
import roomescape.controller.api.dto.response.MemberResponse;

@Component
public class TokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String create(final MemberResponse response) {
        return Jwts.builder()
                .setSubject(String.valueOf(response.id()))
                .claim("name", response.name())
                .claim("role", response.role())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public MemberAuthRequest parse(final String token) {
        final var claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        final var id = Long.parseLong(claims.getSubject());
        final var name = claims.get("name", String.class);
        final var role = claims.get("role", String.class);
        return new MemberAuthRequest(id, name, role);
    }
}

