package roomescape.auth.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.domain.Token;
import roomescape.auth.provider.model.TokenProvider;

@Component
public class JwtTokenProvider implements TokenProvider {

    private static final Date TODAY = new Date();

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Override
    public Token getAccessToken(long principle) {
        String accessToken = Jwts.builder()
                .claims(makeClaims(principle))
                .issuedAt(TODAY)
                .expiration(makeExpiration())
                .signWith(makeKey())
                .compact();

        return new Token(accessToken);
    }

    private Claims makeClaims(long principal) {
        return Jwts.claims()
                .subject(String.valueOf(principal))
                .build();
    }

    private Date makeExpiration() {
        return new Date(TODAY.getTime() + expirationTime);
    }

    private Key makeKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
