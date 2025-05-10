package roomescape.auth.jwt.validator;

import roomescape.auth.jwt.domain.Jwt;

import javax.crypto.SecretKey;

public interface JwtValidator {

    void execute(Jwt token, SecretKey secretKey);
}
