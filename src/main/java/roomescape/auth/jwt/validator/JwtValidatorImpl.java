package roomescape.auth.jwt.validator;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.auth.jwt.domain.Jwt;
import roomescape.auth.jwt.parser.JwtParser;

import javax.crypto.SecretKey;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtValidatorImpl implements JwtValidator {

    private final JwtParser jwtParser;

    @Override
    public void execute(final Jwt token, final SecretKey secretKey) {
        final Claims claims = jwtParser.execute(token, secretKey);
        validateExpiration(claims);
    }

    private void validateExpiration(final Claims claims) {
        if (claims.getExpiration() == null || claims.getExpiration().toInstant().isBefore(Instant.now())) {
            throw new ValidateTokenException();
        }
    }
}
