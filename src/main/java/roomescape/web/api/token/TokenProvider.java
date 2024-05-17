package roomescape.web.api.token;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.util.Optional;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class TokenProvider {
    private static final String EMAIL_FIELD = "email";
    private static final String ROLE_FIELD = "role";

    private final JwtProperties jwtProperties;

    public TokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim(EMAIL_FIELD, member.getEmail())
                .claim(ROLE_FIELD, member.getRole())
                .signWith(HS256, jwtProperties.secretKey().getBytes())
                .compact();
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
}
