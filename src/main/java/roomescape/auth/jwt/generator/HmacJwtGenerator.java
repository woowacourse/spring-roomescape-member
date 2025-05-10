package roomescape.auth.jwt.generator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.auth.jwt.domain.Jwt;
import roomescape.auth.jwt.domain.TokenType;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HmacJwtGenerator implements JwtGenerator {

    public static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

    @Override
    public Jwt execute(final Claims claims,
                       final TokenType type,
                       final SecretKey secretKey) {
        final Instant now = Instant.now();
        final Instant expiration = now.plusMillis(type.getPeriodInMillis());
        final String uniqueId = UUID.randomUUID().toString(); // JTI

        return Jwt.from(
                Jwts.builder()
                        .claims(claims)
                        .id(uniqueId)
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiration))
                        .signWith(secretKey, ALGORITHM)
                        .compact()
        );
    }
}

