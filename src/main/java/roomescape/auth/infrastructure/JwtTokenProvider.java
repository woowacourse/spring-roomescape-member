package roomescape.auth.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.domain.User;

@Component
public class JwtTokenProvider {

    private final String secretKey;

    private final Long expirationInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret.key}") String secretKey,
            @Value("${security.jwt.token.expiration}") Long expirationInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.expirationInMilliseconds = expirationInMilliseconds;
    }

    public String createToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationInMilliseconds);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}
