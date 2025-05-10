package roomescape.auth.jwt.generator;

import io.jsonwebtoken.Claims;
import roomescape.auth.jwt.domain.Jwt;
import roomescape.auth.jwt.domain.TokenType;

import javax.crypto.SecretKey;

public interface JwtGenerator {

    Jwt execute(Claims claims, TokenType type, SecretKey secretKey);
}
