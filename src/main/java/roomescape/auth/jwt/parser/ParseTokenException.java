package roomescape.auth.jwt.parser;

import io.jsonwebtoken.JwtException;

public class ParseTokenException extends JwtException {

    public ParseTokenException(final String message) {
        super(message);
    }
}
