package roomescape.auth.jwt.validator;

import io.jsonwebtoken.JwtException;

public class ValidateTokenException extends JwtException {

    public ValidateTokenException(final String message) {
        super(message);
    }

    public ValidateTokenException() {
        super("");
    }
}
