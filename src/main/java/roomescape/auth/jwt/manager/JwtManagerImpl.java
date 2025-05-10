package roomescape.auth.jwt.manager;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.auth.jwt.domain.Jwt;
import roomescape.auth.jwt.domain.TokenType;
import roomescape.auth.jwt.generator.JwtGenerator;
import roomescape.auth.jwt.parser.JwtParser;
import roomescape.auth.jwt.validator.JwtValidator;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtManagerImpl implements JwtManager {

    private final SecretKey secretKey;
    private final JwtGenerator jwtGenerator;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;

    @Override
    public Jwt generate(final Claims claims, final TokenType type) {
        return jwtGenerator.execute(claims, type, secretKey);
    }

    @Override
    public void validate(final Jwt token) {
        jwtValidator.execute(token, secretKey);
    }

    @Override
    public Claims parse(final Jwt token) {
        return jwtParser.execute(token, secretKey);
    }
}
