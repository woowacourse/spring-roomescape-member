package roomescape.infrastructure;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.controller.api.dto.request.MemberAuthRequest;
import roomescape.controller.api.dto.response.MemberResponse;
import roomescape.exception.CustomBadRequest;

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
        try {
            final var claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            final var id = Long.parseLong(claims.getSubject());
            final var name = claims.get("name", String.class);
            final var role = claims.get("role", String.class);
            return new MemberAuthRequest(id, name, role);
        } catch (final IllegalArgumentException | JwtException e) {
            throw new CustomBadRequest("유효하지 않은 토큰입니다.");
        }
    }
}

