package roomescape.web.api.token;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Role;
import roomescape.web.api.resolver.Principal;

import java.util.Optional;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class TokenParser {
    private static final String EMAIL_FIELD = "email";
    private static final String ROLE_FIELD = "role";

    private final JwtProperties jwtProperties;

    public TokenParser(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Optional<String> extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }

    public Long getId(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(jwtProperties.secretKey().getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.secretKey().getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get(EMAIL_FIELD, String.class);
    }

    public Role getRole(String token) {
        String role = Jwts.parser().setSigningKey(jwtProperties.secretKey().getBytes())
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
