package roomescape.domain.auth;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Role;
import roomescape.web.api.resolver.Principal;

@Component
public class TokenParser {
    private static final String EMAIL_FIELD = "email";
    private static final String ROLE_FIELD = "role";

    @Value("${jwt.secret}")
    private String secretKey;

    public Long getId(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get(EMAIL_FIELD, String.class);
    }

    public Role getRole(String token) {
        String role = Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get(ROLE_FIELD, String.class);

        return Role.from(role);
    }

    public Principal getPrincipal(String token) {
        return new Principal(
                getId(token),
                getEmail(token),
                getRole(token));
    }
}
